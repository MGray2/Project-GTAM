package com.example.gtam.database.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gtam.database.repository.MessageRepository
import com.example.gtam.database.viewmodel.MessageViewModel

class MessageFactory(private val repository: MessageRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}