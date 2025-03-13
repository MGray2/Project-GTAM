package com.example.gtam.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.entities.Client
import com.example.gtam.database.repository.ClientRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ClientViewModel(private val repository: ClientRepository) : ViewModel() {

    val allClients: LiveData<List<Client>> = repository.getAllClients().asLiveData()

    private val _clientDropdownList = MutableLiveData<List<Pair<Long, String>>>()
    val clientDropdownList: LiveData<List<Pair<Long, String>>> get() = _clientDropdownList

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

    fun getClientById(clientId: Long): Deferred<Client?> {
        return viewModelScope.async {
            repository.getClientById(clientId).firstOrNull()
        }
    }

    init {
        allClients.observeForever { clients ->
            _clientDropdownList.value = clients.mapNotNull { client ->
                val displayName = client.clientName.takeIf { it?.isNotEmpty() == true }
                    ?: client.clientAddress.takeIf { it?.isNotEmpty() == true }

                if (displayName != null) {
                    client.id to displayName
                } else {
                    null
                }
            }
        }
    }
}