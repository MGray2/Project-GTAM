package com.example.gtam.database.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.gtam.MessageWorker
import com.example.gtam.database.entities.Message
import com.example.gtam.database.entities.Service
import com.example.gtam.database.repository.MessageRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MessageViewModel(private val repository: MessageRepository) : ViewModel() {

    // Enqueue the worker to process the message
    private fun enqueueWorker(messageId: Long, applicationContext: Context) {
        val workRequest = OneTimeWorkRequestBuilder<MessageWorker>()
            .setInputData(workDataOf("messageId" to messageId))
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }

    // Queue the message and process it with a worker
    fun queueMessage(
        clientId: Long,
        subject: String,
        header: String,
        footer: String,
        services: List<Service>,
        applicationContext: Context,
        onQueued: (Long) -> Unit // callback with inserted message ID
    ) {
        viewModelScope.launch {
            // Convert the services list to a JSON string
            val servicesJson = Gson().toJson(services)

            // Create a new Message instance
            val message = Message(
                clientId = clientId,
                subject = subject,
                header = header,
                footer = footer,
                servicesJson = servicesJson
            )

            // Insert the Message and get the inserted ID
            val id = repository.insertAndGetId(message)
            // Invoke the callback with the inserted ID
            onQueued(id)
            // Enqueue the worker to process the message
            enqueueWorker(id, applicationContext)
        }
    }
}
