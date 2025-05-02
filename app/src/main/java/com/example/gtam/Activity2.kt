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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.gtam.database.factory.ClientFactory
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.components.*
import com.example.gtam.database.viewmodel.ClientViewModel

// Manage Clients
class Activity2 : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val input = Input(Styles())
    private val button = Buttons(Styles())
    private val messages = Strings
    private val dbClients: ClientViewModel by viewModels { ClientFactory(MyApp.clientRepository) }

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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
                        banner.CustomHeader("Manage Clients")
                        // Identity
                        banner.LittleText("Name", modifier = Modifier, button, messages.a2m1)
                        input.InputField(clientName, { clientName = it }, "Client's Name")
                        banner.LittleText("Address", modifier = Modifier, button, messages.a2m2)
                        input.InputField(clientAddress, { clientAddress = it }, "Client's Address")
                        // Email
                        banner.LittleText("Email", modifier = Modifier, button, messages.a2m3)
                        input.InputField(clientEmail,{ clientEmail = it },"Client's Email")
                        // Phone Number
                        banner.LittleText("Phone", modifier = Modifier, button, messages.a2m4)
                        input.InputField(clientPhoneNumber,{ clientPhoneNumber = it },"Client's Phone (no spaces)", KeyboardType.Number)
                        // Save Button
                        button.ButtonGeneric({
                            saveClient(clientName, clientAddress, clientEmail, clientPhoneNumber, dbClients, context, button)
                            clientName = ""
                            clientAddress = ""
                            clientEmail = ""
                            clientPhoneNumber = ""
                        }, "Save")
                        // Window for viewing Clients
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
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

// Formats phone number for client window
private fun phoneFormat(phoneNumber: String): String {
    val digits = phoneNumber.filter { it.isDigit() }
    if (digits.length < 10) return phoneNumber

    val formatted = buildString {
        if (digits.length == 11 && digits.startsWith("1")) {
            append("+1 ")
            append("(${phoneNumber.substring(1,4)})")
            append("${digits.substring(4, 7)}-")
            append(digits.substring(7))
        } else {
            append("(${digits.substring(0, 3)}) ")
            append("${digits.substring(3, 6)}-")
            append(digits.substring(6, 10))
        }
    }
    return formatted
}

// Validation check for email address
private fun isEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(\\.[A-Za-z]{2,})?$")
    return emailRegex.matches(email)
}

// Validation check for phone number
private fun isPhone(phone: String): Boolean {
    val phoneRegex = Regex("^\\+?1?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$")

    return phoneRegex.matches(phone)
}

// Window for displaying the list of Clients
@Composable
private fun ClientWindow(counter: Int, iterable: Client, database: ClientViewModel, button: Buttons) {
    if (counter % 2 != 0) {
        ClientRow(modifier = Modifier.background(MaterialTheme.colorScheme.surface), iterable, database, button)
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
            if (!isEmail(iterable.clientEmail)) {
                Text(text = iterable.clientEmail,
                    modifier = Modifier.weight(1F),
                    fontSize = 20.sp,
                    color = Color.Red)
            } else {
                Text(text = iterable.clientEmail,
                    modifier = Modifier.weight(1F),
                    fontSize = 20.sp)
            }
        }
        if (!iterable.clientPhone.isNullOrBlank()) {
            if (!isPhone(iterable.clientPhone)) {
                Text(text = phoneFormat(iterable.clientPhone),
                    modifier = Modifier.weight(1F),
                    fontSize = 20.sp,
                    color = Color.Red)
            } else {
                Text(text = phoneFormat(iterable.clientPhone),
                    modifier = Modifier.weight(1F),
                    fontSize = 20.sp)
            }
        }
        button.DeleteButton { database.deleteClient(iterable.id) }
    }
}