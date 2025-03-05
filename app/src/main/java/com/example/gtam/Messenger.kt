package com.example.gtam
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import com.example.gtam.database.Service

class Messenger {
    private fun formatBody(header: String, footer: String, services: List<Service>): String {
        val body = StringBuilder("")
        var total = 0.00
        body.append("$header\n")

        services.forEach {
                service -> body.append("${service.serviceName}: ${String.format(Locale.US,"%.2f", service.servicePrice)}\n")
        }

        if (services.size > 1) {
            services.forEach {
                    service -> total += service.servicePrice
                body.append(String.format(Locale.US,"%.2f", total))
            }
        }
        body.append(footer)

        return body.toString()
    }

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
}