package com.example.gtam.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.Client
import com.example.gtam.database.Service
import com.example.gtam.database.UserBot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@SuppressLint("NullSafeMutableLiveData")
class AllViewModel : ViewModel() {
    private val clientDAO = MyApp.database.clientDao()
    private val botDAO = MyApp.database.userBotDao()
    private val serviceDAO = MyApp.database.serviceDao()

    val allClients: LiveData<List<Client>> = clientDAO.getAllClients().asLiveData()
    val allServices: LiveData<List<Service>> = serviceDAO.getAllServices().asLiveData()

    private val _userBot = MutableLiveData<UserBot>()
    val userBot: LiveData<UserBot> get() = _userBot

    private val _clientDropdownList = MutableLiveData<List<Pair<Long, String>>>()
    val clientDropdownList: LiveData<List<Pair<Long, String>>> get() = _clientDropdownList

    init {
        viewModelScope.launch {
            val existingBot = botDAO.getUserBot().firstOrNull()
            if (existingBot == null) {
                val defaultBot = UserBot(
                    id = 1,
                    gmail = null,
                    outlook = null,
                    phoneNumber = null,
                    messageHeader = "",
                    messageFooter = ""
                )
                botDAO.insertOrUpdate(defaultBot)
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
    }


}