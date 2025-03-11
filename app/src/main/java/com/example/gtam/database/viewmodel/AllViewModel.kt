package com.example.gtam.database.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.entities.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@SuppressLint("NullSafeMutableLiveData")
class AllViewModel : ViewModel() {
    private val clientDAO = MyApp.database.clientDao()
    private val botDAO = MyApp.database.userBotDao()
    private val serviceDAO = MyApp.database.serviceDao()
    private val memoryDAO = MyApp.database.memoryDao()

    private val allClients: LiveData<List<Client>> = clientDAO.getAllClients().asLiveData()
    private val allServices: LiveData<List<Service>> = serviceDAO.getAllServices().asLiveData()

    // Bot
    private val _userBot = MutableLiveData<UserBot>()
    val userBot: LiveData<UserBot> get() = _userBot

    // Client
    private val _clientDropdownList = MutableLiveData<List<Pair<Long, String>>>()
    val clientDropdownList: LiveData<List<Pair<Long, String>>> get() = _clientDropdownList

    suspend fun clientById(clientId: Long): Client? {
        return clientDAO.getClientById(clientId).firstOrNull()
    }

    // Service
    private val _serviceDropdownList = MutableLiveData<List<Pair<Long, String>>>()
    val serviceDropdownList: LiveData<List<Pair<Long, String>>> = _serviceDropdownList


    private suspend fun serviceById(serviceId: MutableState<Long?>): Service {
        val nonNullLong: Long = serviceId.value ?: 0L
        return serviceDAO.getServiceById(nonNullLong).firstOrNull() ?: Service(id = nonNullLong, serviceName = "", servicePrice = 0.0, serviceDate = "")
    }

    private suspend fun serviceById(serviceId: Long): Service {
        return serviceDAO.getServiceById(serviceId).firstOrNull() ?: Service(id = serviceId, serviceName = "", servicePrice = 0.0, serviceDate = "")
    }

    private val _selectedServices = MutableLiveData<List<Service>>(emptyList())
    val selectedServices: LiveData<List<Service>> = _selectedServices

    fun addService(serviceId: MutableState<Long?>) {
        viewModelScope.launch {
            val service = serviceById(serviceId)
            _selectedServices.value = _selectedServices.value?.plus(service) ?: listOf(service)
        }
    }

    fun addService(service: Service) {
        _selectedServices.value = _selectedServices.value?.plus(service) ?: listOf(service)
    }

    fun removeService(serviceId: Long) {
        viewModelScope.launch {
            val service = serviceById(serviceId)
            _selectedServices.value = _selectedServices.value?.filterNot { it == service }
        }
    }

    init {
        viewModelScope.launch {
            val existingBot = botDAO.getUserBot().firstOrNull()
            if (existingBot == null) {
                val defaultBot = UserBot(
                    id = 1,
                    email = null,
                    username = null,
                    password = null,
                    phoneNumber = null,
                    messageHeader = "",
                    messageFooter = ""
                )
                botDAO.insertUserBot(defaultBot)
                _userBot.postValue(defaultBot)
            } else {
                _userBot.postValue(existingBot)
            }

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

        allServices.observeForever { services ->
            _serviceDropdownList.value = services.map { it.id to it.serviceName }.toMutableList()
        }

    }

}