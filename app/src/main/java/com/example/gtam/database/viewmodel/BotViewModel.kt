package com.example.gtam.database.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.entities.UserBot
import com.example.gtam.database.repository.UserBotRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("NullSafeMutableLiveData")
class BotViewModel(private val repository: UserBotRepository) : ViewModel() {

    private val _userBot = MutableLiveData<UserBot>()
    val userBot: LiveData<UserBot> get() = _userBot

    init {
        viewModelScope.launch {
            repository.getUserBot().collect { existingBot ->
                if (existingBot == null) {
                    val defaultBot = UserBot(
                        id = 1,
                        email = null,
                        mjApiKey = null,
                        mjSecretKey = null,
                        nvApiKey = null,
                        emailSubject = "",
                        emailHeader = "",
                        emailFooter = "",
                        textHeader = "",
                        textFooter = ""
                    )
                    repository.insertUserBot(defaultBot)
                    _userBot.postValue(defaultBot)
                } else {
                    _userBot.postValue(existingBot)
                }
            }
        }
    }


    fun updateBotInfo(
        email: String,
        mjApiKey: String,
        mjSecretKey: String,
        nvApiKey: String
    ) {
        viewModelScope.launch {
            repository.updateBotInfo(email, mjApiKey, mjSecretKey, nvApiKey)
            val updatedBot = repository.getUserBot().first()
            _userBot.postValue(updatedBot)
        }
    }

    fun updateBotDefaults(
        emailSubject: String,
        emailHeader: String,
        emailFooter: String,
        textHeader: String,
        textFooter: String
    ) {
        viewModelScope.launch {
            repository.updateBotDefaults(emailSubject, emailHeader, emailFooter, textHeader, textFooter)
            val updatedBot = repository.getUserBot().first()
            _userBot.postValue(updatedBot)
        }
    }
}
