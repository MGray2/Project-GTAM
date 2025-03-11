package com.example.gtam.database.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gtam.database.repository.ClientRepository
import com.example.gtam.database.viewmodel.ClientViewModel

class ClientFactory(private val repository: ClientRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}