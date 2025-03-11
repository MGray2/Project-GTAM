package com.example.gtam.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.entities.Client
import com.example.gtam.database.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ClientViewModel(private val repository: ClientRepository) : ViewModel() {

    val allClients: LiveData<List<Client>> = repository.getAllClients().asLiveData()

    fun insertClient(name: String?, address: String?, email: String?, phoneNumber: String?) {
        if (name.isNullOrBlank() && address.isNullOrBlank() && email.isNullOrBlank() && phoneNumber.isNullOrBlank()) {
            return
        }
        viewModelScope.launch {
            val newClient = Client(
                clientName = name,
                clientAddress = address,
                clientEmail = email,
                clientPhoneNumber = phoneNumber)
            repository.insertClient(newClient)
        }
    }

    fun deleteClient(clientId: Long) {
        viewModelScope.launch {
            repository.getClientById(clientId).collect { targetClient ->
                if (targetClient != null) {
                    repository.deleteClient(targetClient)
                }
            }
        }
    }

    fun getClientById(clientId: Long, onResult: (Flow<Client?>) -> Unit) {
        viewModelScope.launch {
            val client = repository.getClientById(clientId)
            onResult(client)
        }
    }
}