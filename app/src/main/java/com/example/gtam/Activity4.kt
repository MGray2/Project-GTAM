package com.example.gtam

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.example.gtam.database.entities.Service
import com.example.gtam.database.entities.UserBot
import com.example.gtam.database.factory.ClientFactory
import com.example.gtam.database.factory.HistoryFactory
import com.example.gtam.database.factory.MemoryFactory
import com.example.gtam.database.factory.ServiceFactory
import com.example.gtam.database.factory.UserBotFactory
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.database.viewmodel.BotViewModel
import com.example.gtam.database.viewmodel.ClientViewModel
import com.example.gtam.database.viewmodel.HistoryViewModel
import com.example.gtam.database.viewmodel.MemoryViewModel
import com.example.gtam.database.viewmodel.ServiceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Calendar


// Compose Message
class Activity4 : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val button = Buttons(Styles())
    private val input = Input(Styles())
    private val userBotVM: BotViewModel by viewModels { UserBotFactory(MyApp.userBotRepository) }
    private val clientVM: ClientViewModel by viewModels { ClientFactory(MyApp.clientRepository) }
    private val serviceVM: ServiceViewModel by viewModels { ServiceFactory(MyApp.serviceRepository) }
    private val memoryVM: MemoryViewModel by viewModels { MemoryFactory(MyApp.memoryRepository) }
    private val historyVM: HistoryViewModel by viewModels { HistoryFactory(MyApp.historyRepository)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Database
            val bot by userBotVM.userBot.observeAsState(initial = UserBot(
                id = 1,
                email = null,
                mjApiKey = null,
                mjSecretKey = null,
                nvApiKey = null,
                messageSubject = "",
                messageHeader = "",
                messageFooter = ""
            ))
            val clientOptions: LiveData<List<Pair<Long, String>>> = clientVM.clientDropdownList
            val serviceOptions: LiveData<List<Pair<Long, String>>> = serviceVM.serviceDropdownList
            val serviceList by serviceVM.selectedServices.observeAsState(emptyList())
            val memory by memoryVM.memory.observeAsState()
            // Local
            val context = LocalContext.current
            val clientSelected = remember { mutableStateOf<Long?>(null) }
            val serviceSelected = remember { mutableStateOf<Long?>(null) }
            var messageSubject by remember { mutableStateOf("") }
            var messageHeader by remember { mutableStateOf("") }
            var messageFooter by remember { mutableStateOf("") }
            var serviceNameWI by remember { mutableStateOf("") }
            var servicePriceWI by remember { mutableStateOf("") }
            var rememberThis by remember { mutableStateOf(true) } // toggle remembering
            var serviceToggle by remember { mutableStateOf(false) } // toggle write-in
            var isText by remember { mutableStateOf(false) } // Hide subject if client uses text

            val resetDropdown1 = remember { mutableStateOf<(() -> Unit)?>(null) }
            val resetDropdown2 = remember { mutableStateOf<(() -> Unit)?>(null) }
            messageSubject = bot.messageSubject
            messageHeader = bot.messageHeader
            messageFooter = bot.messageFooter

            LaunchedEffect(clientSelected.value) {
                clientSelected.value?.let { clientId ->
                    memoryVM.getMemoryByClient(clientId)
                    val client = clientVM.getClientById(clientId).await()
                    isText = client?.clientEmail.isNullOrBlank() && !client?.clientPhone.isNullOrBlank()
                }
            }

            LaunchedEffect(memory) {
                memory?.let {
                    messageSubject = it.subject
                    messageHeader = it.header
                    messageFooter = it.footer
                    serviceVM.getServiceFromMemory(it)
                    Log.d("DataTest", "${it.subject}\n${it.header}\n${it.footer}\n")
                    it.services.forEach { service ->
                        Log.d("DataTest", "Service Name: ${service.serviceName}, Service Price: ${service.servicePrice}, Service Date: ${service.serviceDate}")
                    }
                }
            }
            // UI
            GTAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        banner.CustomHeader("Compose Message")
                        // Message Recipient
                        banner.LittleText("Message Recipient", modifier = Modifier)
                        input.InputDropDown(optionsLiveData = clientOptions, selectedId = clientSelected, "Client", { resetDropdown1.value = it })
                        // Subject
                        if (!isText) {
                            banner.LittleText("Subject", modifier = Modifier)
                            input.InputField(messageSubject, { messageSubject = it }, "Subject")
                        }
                        // Body 1
                        banner.LittleText("Message Header", modifier = Modifier)
                        input.InputFieldLarge(messageHeader, { messageHeader = it }, "Header")
                        // Add Services
                        banner.LittleText("Services", modifier = Modifier)
                        input.InputSwitch(serviceToggle, { serviceToggle = it }, "Write-In")
                        if (serviceToggle) {
                            input.InputField(serviceNameWI, { serviceNameWI = it }, "Write-in Service")
                            input.InputField(servicePriceWI, { servicePriceWI = it }, "Write-in Price", KeyboardType.Decimal)
                        } else {
                            input.InputDropDown(optionsLiveData = serviceOptions, selectedId = serviceSelected, "Add Service") {
                                resetDropdown2.value = it
                            }
                        }

                        button.ButtonGeneric({
                            saveService(serviceVM, serviceSelected, serviceNameWI, servicePriceWI)
                            serviceSelected.value = null
                            serviceNameWI = ""
                            servicePriceWI = ""
                            resetDropdown2.value?.invoke() }, "Add Service")

                        // Service Window
                        Column(modifier = Modifier
                            .fillMaxWidth().padding(10.dp)) {
                            var itemCount = 0
                            serviceList.forEach {
                                    service -> ServiceWindow(itemCount, service, serviceVM, input, button)
                                itemCount++
                            }
                        }


                        // Body 2
                        banner.LittleText("Message Footer", modifier = Modifier)
                        input.InputFieldLarge(messageFooter, { messageFooter = it }, "Footer")

                        input.InputSwitch(rememberThis, { rememberThis = it }, "Remember this interaction")
                        // Send Button
                        button.ButtonGeneric({
                            saveMessage(clientSelected, memoryVM, messageSubject, messageHeader, messageFooter, serviceList, button, context)
                        }, "Save as Draft")
                        button.ButtonGeneric({
                            // Send Email
                            sendMessage(clientVM, bot, historyVM, clientSelected, serviceList, messageSubject, messageHeader, messageFooter)
                            button.showToast("Sending Message", context)
                            // If rememberThis is true, attempt to save preference
                            if (rememberThis) {
                                saveMessage(clientSelected, memoryVM, messageSubject, messageHeader, messageFooter, serviceList, button, context)
                            }
                        }, "Send Message")
                    }
                }
            }
        }
    }
}

private fun saveMessage(
    clientSelected: MutableState<Long?>,
    memoryVM: MemoryViewModel,
    messageSubject: String,
    messageHeader: String,
    messageFooter: String,
    serviceList: List<Service>,
    button: Buttons,
    context: Context
) {
    clientSelected.value?.let { clientId ->
        memoryVM.saveMemory(
        clientId,
        messageSubject,
        messageHeader,
        messageFooter,
        serviceList
            )
    } ?: run { button.showToast("Client not selected, could not save preference.", context)}

}

private fun sendMessage(
    database: ClientViewModel,
    userBot: UserBot,
    history: HistoryViewModel,
    clientSelected: MutableState<Long?>,
    serviceList: List<Service>,
    subject: String,
    header: String,
    footer: String
) {
    CoroutineScope(Dispatchers.IO).launch {
        val clientId = clientSelected.value ?: return@launch
        val client = database.getClientById(clientId).await()
        var response = Messenger.MessengerResponse(true, "No errors detected.")
        var messageType = ""
        var sub = subject // modifiable subject

        // If Email
        if (!client?.clientEmail.isNullOrBlank()) {
            response = Messenger().sendEmail(
                sender = userBot.email ?: return@launch,
                username = userBot.mjApiKey ?: return@launch,
                password = userBot.mjSecretKey ?: return@launch,
                recipient = client!!.clientEmail!!,
                subject = subject,
                header = header,
                services = serviceList,
                footer = footer
            )
            messageType = "Email"
        }
        // If Text
        if (!client?.clientPhone.isNullOrBlank()) {
            response = Messenger().sendSmsEmail(
                sender = userBot.email ?: return@launch,
                apiKey = userBot.mjApiKey ?: return@launch,
                apiSecretKey = userBot.mjSecretKey ?: return@launch,
                recipient = client!!.clientPhone!!,
                numVerifyKey = userBot.nvApiKey ?: return@launch,
                header = header,
                services = serviceList,
                footer = footer
            )
            messageType = "Text"
            sub = "(subject ignored for text)"
        }
        // Create a record of the message
        history.insertHistory(
            clientName = client?.clientName,
            clientAddress = client?.clientAddress,
            clientEmail = client?.clientEmail,
            clientPhone = client?.clientPhone,
            type = messageType,
            status = response.status,
            errorMessage = response.message,
            date = getTodayFullDate(),
            subject = sub,
            body = Messenger().formatBody(header, footer, serviceList)
        )
    }
}

private fun saveService(database: ServiceViewModel, serviceSelected: MutableState<Long?>, serviceNameWI: String, servicePriceWI: String) {
    if (serviceSelected.value != null) {
        database.addService(serviceSelected)
    } else if (serviceNameWI.isNotBlank() && servicePriceWI.isNotBlank()) {
        val newService = Service(id = -System.currentTimeMillis(), serviceName = serviceNameWI, servicePrice = servicePriceWI.toDouble(), serviceDate = null)
        database.addService(newService)
    }
}

private fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

private fun getTodayFullDate(): String {
    val calendar = Calendar.getInstance()
    val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
    val date = SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(calendar.time)
    val time = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(calendar.time)
    return "$day, $date $time"
}

// Window to view the list of Services
@Composable
private fun ServiceWindow(counter: Int, iterable: Service, database: ServiceViewModel, input: Input, button: Buttons) {
    val serviceDateState = remember { mutableStateOf(iterable.serviceDate ?: "") }
    if (counter % 2 != 0) {
        ServiceRow(modifier = Modifier.background(MaterialTheme.colorScheme.surface), iterable, database, input, button, serviceDateState)
    } else {
        ServiceRow(modifier = Modifier, iterable, database, input, button, serviceDateState)
    }
}

// Additional styling for each Service
@Composable
private fun ServiceRow(modifier: Modifier, iterable: Service, database: ServiceViewModel, input: Input, button: Buttons, serviceDateState: MutableState<String>) {
    val rounded = String.format(Locale.US,"%.2f", iterable.servicePrice)
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = iterable.serviceName,
            modifier = Modifier.weight(1F),
            fontSize = 20.sp)
        Text(text = "$${rounded}",
            modifier = Modifier.weight(1F),
            fontSize = 20.sp)
        button.DatePickerButton { iterable.serviceDate = it
        serviceDateState.value = it }
        input.InputFieldSmall(
            serviceDateState.value,
            { serviceDateState.value = it
            iterable.serviceDate = it }, "Date")
        button.RemoveButton { database.removeService(iterable.id) }
    }
}

