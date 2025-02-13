package com.example.gtam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.Client
import kotlinx.coroutines.launch

class ClientViewModel : ViewModel() {
    private val clientDAO = MyApp.database.clientDao()

    val allClients: LiveData<List<Client>> = clientDAO.getAllClients().asLiveData()

    fun insertClient(clientName: String, email: String?, phoneNumber: String?) {
        if (email.isNullOrBlank() && phoneNumber.isNullOrBlank()) {
            return
        }
        viewModelScope.launch {
            val newClient = Client(clientName = clientName,
                clientEmail = email,
                clientPhoneNumber = phoneNumber)
            clientDAO.insertClient(newClient)
        }
    }

    fun deleteClient(clientId: Long) {
        viewModelScope.launch {
            clientDAO.getClientById(clientId).collect { targetClient ->
                if (targetClient != null) {
                    clientDAO.deleteClient(targetClient)
                }
            }
        }
    }
}