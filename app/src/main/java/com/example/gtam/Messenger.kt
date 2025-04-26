package com.example.gtam
import java.util.*
import com.example.gtam.database.entities.Service
import okhttp3.OkHttpClient
import okhttp3.Request
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStreamWriter

class Messenger {
    // Data class to hold information received from NumVerify
    private data class CarrierInfo(
        val valid: Boolean,
        val carrier: String
    )

    // Data class to hold status information for history logging
    data class MessengerResponse(val status: Boolean, val message: String)

    // Connects Header + Service List + Total? + Footer
    fun formatBody(header: String, footer: String, services: List<Service>): String {
        var total = 0.00
        val body = buildAnnotatedString {
            // Header paragraph
            append("$header\n")
            // Services
            services.forEach { service ->
                append("${service.serviceName}:  $${String.format(Locale.US,"%.2f", service.servicePrice)}")
                append( if(!service.serviceDate.isNullOrBlank()) "  ${service.serviceDate}\n" else "\n")
                total += service.servicePrice
            }
            // Total, if necessary
            if (services.size > 1) {
                append("Total Due:  $${String.format(Locale.US,"%.2f", total)}\n")
            }
            // Footer paragraph
            append(footer)
        }
        return body.toString()
    }

    // Watch for special characters, they reduce sending char limit
    @Suppress("SpellCheckingInspection")
    private fun String.containsNonGsmCharacters(): Boolean {
        val gsmCharacters = "@£\$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞ" +
                "ÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?" +
                "¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ`abcdefghijklmnopqrstuvwxyzäöñüà"

        return this.any { it !in gsmCharacters }
    }

    // Replaces or removes strange characters not supported by sms
    private fun normalizeSmsText(text: String): String {
        return text
            .replace('’', '\'')    // Replace fancy apostrophe with simple apostrophe
            .replace('‘', '\'')
            .replace('“', '"')     // Replace fancy quotes with simple quotes
            .replace('”', '"')
            .replace('–', '-')     // Replace fancy dash with simple hyphen
            .replace('—', '-')
            .replace(Regex("[^\\x20-\\x7E\\n]"), "") // Remove any other weird Unicode chars (but keep newlines)
    }

    /* Turns out theres a character limit to SMS,
    this function aims to fragment message into pieces */
    private fun splitSmsMessage(message: String, maxLength: Int = 130): List<String> {
        val words = message.split(Regex("\\s+")) // split by whitespace
        val parts = mutableListOf<String>()
        val currentPart = StringBuilder()

        for (word in words) {
            // +1 for the space
            if (currentPart.length + word.length + 1 > maxLength) {
                parts.add(currentPart.toString().trim())
                currentPart.clear()
            }
            if (currentPart.isNotEmpty()) currentPart.append(' ')
            currentPart.append(word)
        }
        if (currentPart.isNotEmpty()) {
            parts.add(currentPart.toString().trim())
        }

        return parts
    }

    private fun addPartNumbers(parts: List<String>): List<String> {
        if (parts.size <= 1) return parts

        val total = parts.size
        return parts.mapIndexed { index, part ->
            val prefix = "(${index + 1}/$total)\n"
            // Make sure adding prefix won't break limit
            if (prefix.length + part.length > 130) {
                prefix + part.take(130 - prefix.length)
            } else {
                prefix + part
            }
        }
    }

    private fun prepareSmsMessages(body: String): List<String> {
        val normalized = normalizeSmsText(body)
        val rawParts = splitSmsMessage(normalized)
        return addPartNumbers(rawParts)
    }

    // Uses NumVerify API to find the provider of a phone number
    private suspend fun getProvider(phoneNumber: String, apiKey: String): String? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "https://apilayer.net/api/validate?access_key=$apiKey&number=$phoneNumber"

            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string() ?: return@withContext null

                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(CarrierInfo::class.java)
                val carrierInfo = jsonAdapter.fromJson(body)
                return@withContext carrierInfo?.carrier
            }
        }
    }

    // Constructs SMS gateway by reading the result of getProvider()
    @Suppress("SpellCheckingInspection")
    private fun getSmsGateway(phoneNumber: String, carrier: String?): String? {
        val domain = when {
            // AT&T
            (carrier?.contains(Regex("at[&\\s]?t", RegexOption.IGNORE_CASE)) == true) -> "txt.att.net"
            // Verizon Phones
            (carrier?.contains(Regex("verizon|xfinity|visible", RegexOption.IGNORE_CASE)) == true) -> "vtext.com"
            // T-Mobile
            (carrier?.contains(Regex("t[-\\s]?mobile", RegexOption.IGNORE_CASE)) == true ) -> "tmomail.net"
            // Sprint
            (carrier?.contains(Regex("sprint", RegexOption.IGNORE_CASE)) == true) -> "messaging.sprintpcs.com"
            // U.S. Cellular
            (carrier?.contains(Regex("u[.\\s]?s[.\\s]?cellular", RegexOption.IGNORE_CASE)) == true) -> "email.uscc.net"
            // Boost Mobile
            (carrier?.contains(Regex("boost", RegexOption.IGNORE_CASE)) == true) -> "sms.myboostmobile.com"
            // Cricket Wireless
            (carrier?.contains(Regex("cricket", RegexOption.IGNORE_CASE)) == true) -> "sms.cricketwireless.net"
            // Metro by T-Mobile
            (carrier?.contains(Regex("metro", RegexOption.IGNORE_CASE)) == true) -> "mymetropcs.com"
            // Google Fi
            (carrier?.contains(Regex("google", RegexOption.IGNORE_CASE)) == true) -> "msg.fi.google.com"
            else -> null
        }

        return domain?.let { "$phoneNumber@$it" }
    }

    // Check if emails are correct
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    // Send messages using the Mailjet API
    private suspend fun sendMailjetEmail(
        sender: String,
        recipient: String,
        subject: String,
        body: String,
        apiKey: String,
        apiSecretKey: String
    ): MessengerResponse {
        return withContext(Dispatchers.IO) {
            try {
                // incomplete API information
                if (apiKey.isBlank() || apiSecretKey.isBlank()) {
                    return@withContext MessengerResponse(false, "API key or Secret Key is missing")
                }
                if (!isValidEmail(sender)) {
                    return@withContext MessengerResponse(false, "Sender email is invalid")
                }
                if (!isValidEmail(recipient)) {
                    return@withContext MessengerResponse(false, "Recipient email is invalid")
                }

                val url = URL("https://api.mailjet.com/v3.1/send")
                val connection = url.openConnection() as HttpURLConnection

                val credentials = "$apiKey:$apiSecretKey"
                val authHeader = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", authHeader)
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonBody = JSONObject().apply {
                    put("Messages", JSONArray().put(
                        JSONObject().apply {
                            put("From", JSONObject().put("Email", sender))
                            put("To", JSONArray().put(JSONObject().put("Email", recipient)))
                            put("Subject", subject)
                            put("TextPart", body)
                        }
                    ))
                }.toString()

                OutputStreamWriter(connection.outputStream).use { it.write(jsonBody) }

                val responseCode = connection.responseCode

                return@withContext MessengerResponse(responseCode in 200..299, "") // success
            } catch (e: IOException) {
                e.printStackTrace()
                val errorMessage = "${e.message}"
                return@withContext MessengerResponse(false, errorMessage)
            }
        }
    }

    // Mailjet ApiKey, Mailjet SecretKey and sender and recipient are email addresses. returns messenger response
    suspend fun sendEmail(username: String, password: String, sender: String, recipient: String, subject: String, header: String, services: List<Service>, footer: String): MessengerResponse {
        val body = formatBody(header, footer, services)
        return sendMailjetEmail(sender, recipient, subject, body, username, password)
    }

    //  Mailjet apiKey, Mailjet SecretKey, sender is an email, recipient is a phone number. returns messenger response
    suspend fun sendSmsEmail(apiKey: String, apiSecretKey: String, sender: String, recipient: String, numVerifyKey: String, header: String, services: List<Service>, footer: String): MessengerResponse {
        var phoneNumber = recipient
        if (phoneNumber.length == 10) phoneNumber = "1$phoneNumber" // Add country number

        val body = formatBody(header, footer, services)
        var provider = getProvider(phoneNumber, numVerifyKey)
        if (provider == null) provider = getProvider(recipient, numVerifyKey) // try original number
        if (phoneNumber.length == 11) phoneNumber = phoneNumber.substring(1) // Remove country number

        val smsGateway = getSmsGateway(phoneNumber, provider)
            ?: return MessengerResponse(false, "Failed to create SMS gateway.") // does not need country number

        val parts = prepareSmsMessages(body)

        parts.forEach { part ->
            sendMailjetEmail(sender, smsGateway, "", part, apiKey, apiSecretKey)
            delay(2000)
        }

        return MessengerResponse(true, "")
    }
}