package com.example.gtam
import android.util.Log
import java.util.*
import com.example.gtam.database.entities.Service
import okhttp3.OkHttpClient
import okhttp3.Request
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import androidx.lifecycle.ViewModel
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
        val body = StringBuilder("")
        var total = 0.00
        body.append("$header\n")

        services.forEach {
                service -> body.append("${service.serviceName}: $${String.format(Locale.US,"%.2f", service.servicePrice)}   ${service.serviceDate}\n")
                total += service.servicePrice
        }

        if (services.size > 1) {
            body.append("Total: $${String.format(Locale.US,"%.2f", total)}\n")
        }
        body.append(footer)

        return body.toString()
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
                Log.d("DataTest", "Acquired Carrier Info: ${carrierInfo?.carrier}")
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
        Log.d("DataTest", "SMS Gateway: $domain")

        return domain?.let { "$phoneNumber@$it" }
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
                    Log.e("MailjetError", "API key or Secret is missing!")
                    return@withContext MessengerResponse(false, "API key or Secret Key is missing")
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
                val responseMessage = try {
                    connection.inputStream.bufferedReader().readText()
                } catch (e: IOException) {
                    connection.errorStream?.bufferedReader()?.readText() ?: "Unknown error"
                }

                Log.d("MailjetResponse", "Code: $responseCode, Message: $responseMessage")

                return@withContext MessengerResponse(responseCode in 200..299, "") // success
            } catch (e: IOException) {
                e.printStackTrace()
                val errorMessage = "${e.message}"
                Log.e("MailjetError", "Error sending email: ${e.message}")
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

        val smsGateway = getSmsGateway(phoneNumber, provider) // does not need country number

        return if (smsGateway != null) {
            sendMailjetEmail(sender, smsGateway, "", body, apiKey, apiSecretKey)
        } else MessengerResponse(false, "Failed to create SMS gateway.")
    }
}