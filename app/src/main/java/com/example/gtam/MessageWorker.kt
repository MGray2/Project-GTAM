package com.example.gtam

import android.content.Context import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gtam.database.entities.History
import com.example.gtam.database.entities.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val messageId = inputData.getLong("messageId", -1L)
        if (messageId == -1L) return Result.failure()

        val message = MyApp.messageRepository.getById(messageId)
            ?: return Result.failure()

        val userBot = MyApp.userBotRepository.getUserBotOnce()
            ?: return Result.failure()

        val client = MyApp.clientRepository.getClientByIdOnce(message.clientId)
            ?: return Result.failure()

        val services: List<Service> = try {
            Gson().fromJson(
                message.servicesJson,
                object : TypeToken<List<Service>>() {}.type
            )
        } catch (e: Exception) {
            return Result.failure()
        }

        var response = Messenger.MessengerResponse(true, "No errors.")
        var messageType = ""
        var subject = message.subject
        val messenger = Messenger()

        // Send Email if applicable
        if (!client.clientEmail.isNullOrBlank()) {
            response = messenger.sendEmail(
                sender = userBot.email ?: return Result.failure(),
                username = userBot.mjApiKey ?: return Result.failure(),
                password = userBot.mjSecretKey ?: return Result.failure(),
                recipient = client.clientEmail,
                subject = subject,
                header = message.header,
                services = services,
                footer = message.footer
            )
            messageType = "Email"
        }

        // Send SMS if applicable
        if (!client.clientPhone.isNullOrBlank()) {
            response = messenger.sendSmsEmail(
                sender = userBot.email ?: return Result.failure(),
                apiKey = userBot.mjApiKey ?: return Result.failure(),
                apiSecretKey = userBot.mjSecretKey ?: return Result.failure(),
                recipient = client.clientPhone,
                numVerifyKey = userBot.nvApiKey ?: return Result.failure(),
                header = message.header,
                services = services,
                footer = message.footer
            )
            messageType = "Text"
            subject = "(subject ignored for text)"
        }

        // Save to History
        MyApp.historyRepository.insertHistory(History(
            clientName = client.clientName,
            clientAddress = client.clientAddress,
            clientEmail = client.clientEmail,
            clientPhone = client.clientPhone,
            type = messageType,
            status = response.status,
            errorMessage = response.message,
            date = Date.getTodayFullDate(),
            subject = subject,
            body = Messenger().formatBody(message.header, message.footer, services)
        ))

        // Remove after operation
        MyApp.messageRepository.delete(message)

        return if (response.status) Result.success() else Result.retry()
    }
}