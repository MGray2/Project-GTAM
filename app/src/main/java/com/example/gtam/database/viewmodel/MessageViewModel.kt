package com.example.gtam.database.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.workDataOf
import com.example.gtam.Messenger
import com.example.gtam.database.entities.Service
import com.example.gtam.database.entities.UserBot
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    fun sendMessage(
        database: ClientViewModel,
        userBot: UserBot,
        history: HistoryViewModel,
        clientSelected: MutableState<Long?>,
        serviceList: List<Service>,
        subject: String,
        header: String,
        footer: String,
        date: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val clientId = clientSelected.value ?: return@launch
            val client = database.getClientById(clientId).await()
            val messenger = Messenger()
            var response = Messenger.MessengerResponse(true, "No errors detected.")
            var messageType = ""
            var sub = subject

            if (!client?.clientEmail.isNullOrBlank()) {
                response = messenger.sendEmail(
                    sender = userBot.email ?: return@launch,
                    username = userBot.mjApiKey ?: return@launch,
                    password = userBot.mjSecretKey ?: return@launch,
                    recipient = client!!.clientEmail!!,
                    subject = subject,
                    header = header,
                    services = serviceList,
                    footer = footer
                )
                messageType = "Email"
            } else if (!client?.clientPhone.isNullOrBlank()) {
                response = messenger.sendSmsEmail(
                    sender = userBot.email ?: return@launch,
                    apiKey = userBot.mjApiKey ?: return@launch,
                    apiSecretKey = userBot.mjSecretKey ?: return@launch,
                    recipient = client!!.clientPhone!!,
                    numVerifyKey = userBot.nvApiKey ?: return@launch,
                    header = header,
                    services = serviceList,
                    footer = footer
                )
                messageType = "Text"
                sub = "(subject ignored for text)"
            }

            history.insertHistory(
                clientName = client?.clientName,
                clientAddress = client?.clientAddress,
                clientEmail = client?.clientEmail,
                clientPhone = client?.clientPhone,
                type = messageType,
                status = response.status,
                errorMessage = response.message,
                date = date,
                subject = sub,
                body = messenger.formatBody(header, footer, serviceList)
            )
        }
    }

    val gson = Gson()

    val data = workDataOf(
        "client" to gson.toJson(client), // Client object
        "subject" to subject,
        "header" to header,
        "footer" to footer,
        "services" to gson.toJson(serviceList) // List<Service>
    )

    val workRequest = OneTimeWorkRequestBuilder<MessageWorker>()
        .setInputData(data)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}