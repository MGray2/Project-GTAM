package com.example.gtam

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.Client
import com.example.gtam.database.Service
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.Components
import kotlinx.coroutines.launch
import java.util.Locale

// Manage Clients
class Activity2 : ComponentActivity() {
    private val component = Components()
    private val dbClients: ClientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                Column (modifier = Modifier) {
                    val context = LocalContext.current
                    var clientName by remember { mutableStateOf("") }
                    var clientEmail by remember { mutableStateOf("") }
                    var clientPhoneNumber by remember { mutableStateOf("") }
                    val clientList by dbClients.allClients.observeAsState(initial = emptyList())

                    component.CustomHeader("Manage Clients")
                    component.LittleText("Client's Name", modifier = Modifier)
                    component.InputField(clientName, { clientName = it }, "Name")
                    component.LittleText("Client's Email", modifier = Modifier)
                    component.InputField(clientEmail,{ clientEmail = it },"Email")
                    component.LittleText("Client's Phone Number", modifier = Modifier)
                    component.InputFieldNumber(clientPhoneNumber,{ clientPhoneNumber = it },"Phone Number", KeyboardType.Number)
                    component.ButtonGeneric({
                        saveClient(clientName, clientEmail, clientPhoneNumber, dbClients, context, component)
                    }, "Save")
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                            .fillMaxWidth().padding(10.dp)
                    ) {
                        var itemCount = 0
                        clientList.forEach { client ->
                            ClientWindow(itemCount, client, dbClients, component)
                            itemCount++
                        }
                    }
                }

            }
        }
    }
}

class ClientViewModel : ViewModel() {
    private val clientDAO = MyApp.database.clientDao()

    val allClients: LiveData<List<Client>> = clientDAO.getAllClients().asLiveData()

    fun insertClient(clientName: String, email: String?, phoneNumber: String?) {
        if (email.isNullOrBlank() && phoneNumber.isNullOrBlank()) {
            return
        }
        viewModelScope.launch {
            val newClient = Client(clientName = clientName,
                clientEmail = email,
                clientPhoneNumber = phoneNumber)
            clientDAO.insertClient(newClient)
        }
    }

    fun deleteClient(clientId: Long) {
        viewModelScope.launch {
            clientDAO.getClientById(clientId).collect { targetClient ->
                if (targetClient != null) {
                    clientDAO.deleteClient(targetClient)
                }
            }
        }
    }
}

private fun saveClient(clientName: String, clientEmail: String, clientPhoneNumber: String, database: ClientViewModel, context: Context, component: Components) {
    if (clientName.isNotBlank()) {
        if (clientEmail.isNotBlank() || clientPhoneNumber.isNotBlank()) {
            database.insertClient(clientName, clientEmail, clientPhoneNumber)
        } else {
            component.ShowToast("Missing email or phone number.", context)
        }
    } else {
        component.ShowToast("Missing client name.", context)
    }
}

@Composable
private fun ClientWindow(counter: Int, iterable: Client, database: ClientViewModel, component: Components) {
    if (counter % 2 != 0) {
        ClientRow(modifier = Modifier.background(Color.LightGray), iterable, database, component)
    } else {
        ClientRow(modifier = Modifier, iterable, database, component)
    }
}

@Composable
private fun ClientRow(modifier: Modifier, iterable: Client, database: ClientViewModel, component: Components) {
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = iterable.clientName,
            modifier = Modifier.weight(1F),
            fontSize = 20.sp)
        if (!iterable.clientEmail.isNullOrBlank()) {
            Text(text = iterable.clientEmail,
                modifier = Modifier.weight(1F),
                fontSize = 20.sp)
        }
        if (!iterable.clientPhoneNumber.isNullOrBlank()) {
            Text(text = "#${iterable.clientPhoneNumber}",
                modifier = Modifier.weight(1F),
                fontSize = 20.sp)
        }
        component.DeleteButton { database.deleteClient(iterable.id) }
    }
}