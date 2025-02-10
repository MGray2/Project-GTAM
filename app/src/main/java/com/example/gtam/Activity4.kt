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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.gtam.database.Client
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme

// Compose Message
class Activity4 : ComponentActivity() {
    private val component = Components()
    private val dbAll: AllViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                val clientList by dbAll.allClients.observeAsState(initial = emptyList())
                val clientOptions = clientList.map { it.id to it.clientName }
                val clientSelected = remember { mutableStateOf<Long?>(null) }
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

class AllViewModel : ViewModel() {
    private val clientDAO = MyApp.database.clientDao()

    val allClients: LiveData<List<Client>> = clientDAO.getAllClients().asLiveData()
}