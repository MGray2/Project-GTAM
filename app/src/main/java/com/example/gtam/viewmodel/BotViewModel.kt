package com.example.gtam.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.UserBot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@SuppressLint("NullSafeMutableLiveData")
class BotViewModel : ViewModel() {
    private val botDAO = MyApp.database.userBotDao()

    private val _userBot = MutableLiveData<UserBot>()
    val userBot: LiveData<UserBot> get() = _userBot

    init {
        viewModelScope.launch {
            val existingBot = botDAO.getUserBot().firstOrNull()
            if (existingBot == null) {
                val defaultBot = UserBot(
                    id = 1,
                    gmail = null,
                    gmailPassword = null,
                    outlook = null,
                    outlookPassword = null,
                    phoneNumber = null,
                    messageHeader = "",
                    messageFooter = ""
                )
                botDAO.insertOrUpdate(defaultBot)
                _userBot.postValue(defaultBot)
            } else {
                _userBot.postValue(existingBot)
            }
        }
    }


    // Update changes to UserBot
    fun updateBot(gmail: String, gmailPassword: String, outlook: String, outlookPassword: String, phoneNumber: String, messageHeader: String, messageFooter: String) {
        viewModelScope.launch {
            botDAO.updateUserBot(
                gmail = gmail,
                gmailPassword = gmailPassword,
                outlook = outlook,
                outlookPassword = outlookPassword,
                phoneNumber = phoneNumber,
                messageHeader = messageHeader,
                messageFooter = messageFooter)
        }
    }
}