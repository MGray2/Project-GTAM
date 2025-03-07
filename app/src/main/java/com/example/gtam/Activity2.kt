package com.example.gtam

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import com.example.gtam.database.entities.Client
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.components.*
import com.example.gtam.database.viewmodel.ClientViewModel

// Manage Clients
class Activity2 : ComponentActivity() {
    // Global
    private val banner = Banners()
    private val input = Input()
    private val button = Buttons()
    private val dbClients: ClientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val context = LocalContext.current
            var clientName by remember { mutableStateOf("") }
            var clientAddress by remember { mutableStateOf("") }
            var clientEmail by remember { mutableStateOf("") }
            var clientPhoneNumber by remember { mutableStateOf("") }
            // Database
            val clientList by dbClients.allClients.observeAsState(initial = emptyList())

            // UI
            GTAMTheme {
                Column (modifier = Modifier) {
                    banner.CustomHeader("Manage Clients")
                    // Identity
                    banner.LittleText("Client's Name / Address", modifier = Modifier)
                    input.InputField(clientName, { clientName = it }, "Name")
                    input.InputField(clientAddress, { clientAddress = it }, "Address")
                    // Email
                    banner.LittleText("Client's Email", modifier = Modifier)
                    input.InputField(clientEmail,{ clientEmail = it },"Email")
                    // Phone Number
                    banner.LittleText("Client's Phone Number", modifier = Modifier)
                    input.InputFieldNumber(clientPhoneNumber,{ clientPhoneNumber = it },"Phone Number", KeyboardType.Number)
                    // Save Button
                    button.ButtonGeneric({
                        saveClient(clientName, clientAddress, clientEmail, clientPhoneNumber, dbClients, context, button)
                    }, "Save")
                    // Window for viewing Clients
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                            .fillMaxWidth().padding(10.dp)
                    ) {
                        var itemCount = 0
                        clientList.forEach { client ->
                            ClientWindow(itemCount, client, dbClients, button)
                            itemCount++
                        }
                    }
                }

            }
        }
    }
}

// Outsourced functionality for save button
private fun saveClient(clientName: String?, clientAddress: String?, clientEmail: String?, clientPhoneNumber: String?, database: ClientViewModel, context: Context, button: Buttons) {
    if (!clientName.isNullOrBlank() || !clientAddress.isNullOrBlank()) {
        if (!clientEmail.isNullOrBlank() || !clientPhoneNumber.isNullOrBlank()) {
            database.insertClient(clientName, clientAddress, clientEmail, clientPhoneNumber)
        } else {
            button.showToast("Missing email or phone number.", context)
        }
    } else {
        button.showToast("Missing Name or Address.", context)
    }
}

// Window for displaying the list of Clients
@Composable
private fun ClientWindow(counter: Int, iterable: Client, database: ClientViewModel, button: Buttons) {
    if (counter % 2 != 0) {
        ClientRow(modifier = Modifier.background(Color.LightGray), iterable, database, button)
    } else {
        ClientRow(modifier = Modifier, iterable, database, button)
    }
}

// Additional styling for each client
@Composable
private fun ClientRow(modifier: Modifier, iterable: Client, database: ClientViewModel, button: Buttons) {
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        if (!iterable.clientName.isNullOrBlank()) {
            Text(text = iterable.clientName,
                modifier = Modifier.weight(1F),
                fontSize = 20.sp)
        } else if (!iterable.clientAddress.isNullOrBlank()){
            Text(text = iterable.clientAddress,
                modifier = Modifier.weight(1F),
                fontSize = 20.sp)
        }
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
        button.DeleteButton { database.deleteClient(iterable.id) }
    }
}