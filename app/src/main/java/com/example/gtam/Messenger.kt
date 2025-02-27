package com.example.gtam
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.*

class Messenger {

    fun sendEmail(sender: String, appPassword: String, recipient: String, sub: String, body: String): Boolean {

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
}