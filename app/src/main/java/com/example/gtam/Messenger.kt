package com.example.gtam
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import com.example.gtam.database.entities.Service
import okhttp3.OkHttpClient
import okhttp3.Request
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Messenger {

    // Data class to hold information received from NumVerify
    private data class CarrierInfo(
        val valid: Boolean,
        val carrier: String
    )

    // Connects Header + Service List + Total? + Footer
    private fun formatBody(header: String, footer: String, services: List<Service>): String {
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

    @Suppress("SpellCheckingInspection")
    private fun getSmsGateway(phoneNumber: String, carrier: String?): String? {
        val domain = when (carrier?.lowercase()) {
            "at&t", "att wireless", "at&t mobility llc" -> "txt.att.net"
            "verizon", "verizon wireless", "cellco partnership (verizon wireless)" -> "vtext.com"
            "t-mobile", "tmobile" -> "tmomail.net"
            "sprint", "sprint pcs" -> "messaging.sprintpcs.com"
            "us cellular" -> "email.uscc.net"
            else -> null
        }
        Log.d("DataTest", "SMS Gateway: $domain")

        return domain?.let { "$phoneNumber@$it" }
    }

    // Send messages through Gmail API
    private fun sendGmail(sender: String, appPassword: String, recipient: String, sub: String, body: String): Boolean {

        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(sender, appPassword)
        })

        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(sender))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                subject = sub
                setText(body)
            }
            Transport.send(message)
            Log.d("DataTest","Email sent successfully to $recipient")
            true
        } catch (e: MessagingException) {
            e.printStackTrace()
            false
        }
    }

    // Send messages using the Mailjet API
    private fun sendMailjetEmail(sender: String, recipient: String, sub: String, body: String, username: String, password: String): Boolean {

        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "in-v3.mailjet.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(username, password)
        })

        return try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(sender))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                subject = sub
                setText(body)
            }
            Transport.send(message)
            Log.d("DataTest","Email sent successfully!") // Debug
            true
        } catch (e: MessagingException) {
            e.printStackTrace()
            false
        }
    }

    // Username is an api key, password is the secret key and sender and recipient are email addresses
    fun sendEmail(username: String, password: String, sender: String, recipient: String, sub: String, header: String, services: List<Service>, footer: String) {
        val body = formatBody(header, footer, services)
        sendMailjetEmail(sender, recipient, sub, body, username, password)
    }

    // Username is an api key, password is the secret key, sender is an email, recipient is a phone number
    fun sendSmsEmail(username: String, password: String, sender: String, recipient: String, key: String, header: String, services: List<Service>, footer: String) {
        var phoneNumber = recipient
        Log.d("DataTest", "Phone# start:$phoneNumber")
        if (phoneNumber.length == 10) {
            phoneNumber = "1$phoneNumber"
        }
        Log.d("DataTest", "Phone# if:$phoneNumber")
        CoroutineScope(Dispatchers.IO).launch {
            val body = formatBody(header, footer, services)
            val provider = getProvider(phoneNumber, key) // needs 1+phoneNumber
            if (phoneNumber.length == 11) phoneNumber = phoneNumber.substring(1)
            Log.d("DataTest", "Phone# cs:$phoneNumber")
            val smsGateway = getSmsGateway(phoneNumber, provider) // does not need 1+

            if (smsGateway != null) {
                sendMailjetEmail(sender, smsGateway, "", body, username, password)
            }
        }
    }
}