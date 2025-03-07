package com.example.gtam.database.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.entities.UserBot
import com.example.gtam.database.repository.UserBotRepository
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
                        username = null,
                        password = null,
                        phoneNumber = null,
                        messageHeader = "",
                        messageFooter = ""
                    )
                    repository.insertUserBot(defaultBot)
                    _userBot.postValue(defaultBot)
                } else {
                    _userBot.postValue(existingBot)
                }
            }
        }
    }


    // Update changes to UserBot
    fun updateBot(
        email: String,
        username: String,
        password: String,
        phoneNumber: String,
        messageHeader: String,
        messageFooter: String
    ) {
        viewModelScope.launch {
            val updatedBot = UserBot(
                id = 1,
                email = email,
                username = username,
                password = password,
                phoneNumber = phoneNumber,
                messageHeader = messageHeader,
                messageFooter = messageFooter
            )
            repository.updateUserBot(updatedBot)
            _userBot.postValue(updatedBot)
        }
    }
}
