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
import androidx.compose.ui.Modifier
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.viewmodel.AllViewModel

// Compose Message
class Activity4 : ComponentActivity() {
    // Global
    private val component = Components()
    private val dbAll: AllViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Database
            val clientList by dbAll.allClients.observeAsState(initial = emptyList())
            // Local
            val clientOptions = clientList.map { it.id to it.clientName }
            val clientSelected = remember { mutableStateOf<Long?>(null) }

            // UI
            GTAMTheme {
                Column() {
                    component.CustomHeader("Compose Message")
                    component.LittleText("Message Recipient", modifier = Modifier)
                    component.InputDropDown(options = clientOptions, selectedId = clientSelected, "Client")
                    component.ButtonGeneric({ Log.d("DataTest", "$clientSelected") }, "Test")
                }

            }
        }
    }
}

