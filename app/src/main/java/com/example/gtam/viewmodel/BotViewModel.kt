package com.example.gtam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.UserBot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class BotViewModel : ViewModel() {
    private val botDAO = MyApp.database.userBotDao()

    private val _userBot = MutableLiveData<UserBot>()
    val userBot: LiveData<UserBot> get() = _userBot

    init {
        viewModelScope.launch {
            _userBot.value = getOrCreateBot()
        }
    }

    // Find instance of UserBot or else create one
    private suspend fun getOrCreateBot(): UserBot {
        val existingBot = botDAO.getUserBot().firstOrNull()
        return if (existingBot != null) {
            existingBot
        } else {
            val defaultBot = UserBot(
                id = 1,
                gmail = null,
                outlook = null,
                phoneNumber = null,
                messageHeader = "",
                messageFooter = ""
            )
            botDAO.insertOrUpdate(defaultBot)
            defaultBot
        }
    }

    // Update changes to UserBot
    fun updateBot(gmail: String, outlook: String, phoneNumber: String, messageHeader: String, messageFooter: String) {
        viewModelScope.launch {
            botDAO.updateUserBot(gmail = gmail,
                outlook = outlook,
                phoneNumber = phoneNumber,
                messageHeader = messageHeader,
                messageFooter = messageFooter)
        }
    }
}