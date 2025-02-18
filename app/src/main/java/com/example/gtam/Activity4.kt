package com.example.gtam

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import com.example.gtam.database.Client
import com.example.gtam.database.UserBot
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.viewmodel.AllViewModel

// Compose Message
class Activity4 : ComponentActivity() {
    // Global
    private val banner = Banners()
    private val button = Buttons()
    private val input = Input()
    private val dbAll: AllViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Database
            val allClients: LiveData<List<Client>> = dbAll.allClients
            val serviceList by dbAll.allServices.observeAsState(initial = emptyList())
            val bot by dbAll.userBot.observeAsState(initial = UserBot(id = 1, gmail = "", outlook = "", phoneNumber = "", messageHeader = "", messageFooter = ""))
            val clientOptions: LiveData<List<Pair<Long, String>>> = dbAll.clientDropdownList
            // Local
            val clientSelected = remember { mutableStateOf<Long?>(null) }
            var messageHeader by remember { mutableStateOf("") }
            var messageFooter by remember { mutableStateOf("") }
            messageHeader = bot.messageHeader
            messageFooter = bot.messageFooter
            // UI
            GTAMTheme {
                Column() {
                    banner.CustomHeader("Compose Message")
                    // Message Recipient
                    banner.LittleText("Message Recipient", modifier = Modifier)
                    input.InputDropDown(optionsLiveData = clientOptions, selectedId = clientSelected, "Client")
                    // Body 1
                    banner.LittleText("Message Header", Modifier)
                    input.InputFieldLarge(messageHeader, { messageHeader = it }, "Header")

                    // Body 2
                    banner.LittleText("Message Footer", modifier = Modifier)
                    input.InputFieldLarge(messageFooter, { messageFooter = it }, "Footer")

                    // Test Button
                    button.ButtonGeneric({ Log.d("DataTest", "$clientSelected") }, "Test")
                }

            }
        }
    }
}

