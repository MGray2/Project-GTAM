package com.example.gtam.activities

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.gtam.MyApp
import com.example.gtam.database.entities.Service
import com.example.gtam.database.entities.UserBot
import com.example.gtam.database.factory.ClientFactory
import com.example.gtam.database.factory.MemoryFactory
import com.example.gtam.database.factory.MessageFactory
import com.example.gtam.database.factory.ServiceFactory
import com.example.gtam.database.factory.UserBotFactory
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.database.viewmodel.*
import java.util.Locale


// Compose Message
class Activity4 : ComponentActivity() {
    // Global
    private val styles = Styles()
    private val banner = Banners(styles)
    private val button = Buttons(styles)
    private val input = Input(styles)
    private val userBotVM: BotViewModel by viewModels { UserBotFactory(MyApp.userBotRepository) }
    private val clientVM: ClientViewModel by viewModels { ClientFactory(MyApp.clientRepository) }
    private val serviceVM: ServiceViewModel by viewModels { ServiceFactory(MyApp.serviceRepository) }
    private val memoryVM: MemoryViewModel by viewModels { MemoryFactory(MyApp.memoryRepository) }
    private val messageVM: MessageViewModel by viewModels { MessageFactory(MyApp.messageRepository) }
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
                emailSubject = "",
                emailHeader = "",
                emailFooter = "",
                textHeader = "",
                textFooter = ""
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
            messageSubject = bot.emailSubject
            messageHeader = bot.emailHeader
            messageFooter = bot.emailFooter

            // Listen for changes in clientSelected
            LaunchedEffect(clientSelected.value) {
                // Remove persisting data when dropdown clears
                if (clientSelected.value == null) {
                    memoryVM.clearMemory()
                    serviceVM.clearServiceSelected()
                }
                clientSelected.value?.let { clientId ->
                    memoryVM.getMemoryByClient(clientId)
                    val client = clientVM.getClientById(clientId).await()
                    isText = client?.clientEmail.isNullOrBlank() && !client?.clientPhone.isNullOrBlank()

                    if (memory == null) {
                        serviceVM.clearServiceSelected()
                        // Use default data based on client contact
                        userBotVM.userBot.value?.let { bot ->
                            if (!isText) {
                                messageSubject = bot.emailSubject
                                messageHeader = bot.emailHeader
                                messageFooter = bot.emailFooter
                            } else {
                                messageSubject = "" // texts don’t use a subject
                                messageHeader = bot.textHeader
                                messageFooter = bot.textFooter
                            }
                        }
                    }
                }
            }

            // Listen for changes in memory
            LaunchedEffect(memory) {
                memory?.let {
                    messageSubject = it.subject
                    messageHeader = it.header
                    messageFooter = it.footer
                    serviceVM.getServiceFromMemory(it)
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

                        // Save as draft
                        button.ButtonGeneric({
                            if (saveMessage(clientSelected, memoryVM, messageSubject, messageHeader, messageFooter, serviceList, button, context))
                                button.showToast("Message saved as draft.", context)
                            resetDropdown1.value?.invoke() }, "Save as Draft")

                        // Send Message
                        button.ButtonGeneric({
                            clientSelected.value?.let { clientId ->
                                messageVM.queueMessage(clientId, messageSubject, messageHeader, messageFooter, serviceList, context) {
                                    button.showToast("Sending Message", context)
                                }
                            }

                            // If rememberThis is true, attempt to save preference
                            if (rememberThis) {
                                saveMessage(clientSelected, memoryVM, messageSubject, messageHeader, messageFooter, serviceList, button, context)
                            }
                            resetDropdown1.value?.invoke()
                        }, "Send Message")
                    }
                }
            }
        }
    }

    // Remember this interaction
    private fun saveMessage(
        clientSelected: MutableState<Long?>,
        memoryVM: MemoryViewModel,
        messageSubject: String,
        messageHeader: String,
        messageFooter: String,
        serviceList: List<Service>,
        button: Buttons,
        context: Context
    ): Boolean {
        clientSelected.value?.let { clientId ->
            memoryVM.saveMemory(
                clientId,
                messageSubject,
                messageHeader,
                messageFooter,
                serviceList
            )
            return true
        } ?: run {
            button.showToast("Client not selected, could not save preference.", context)
            return false
        }
    }

    // Helper to store services to memory
    private fun saveService(database: ServiceViewModel, serviceSelected: MutableState<Long?>, serviceNameWI: String, servicePriceWI: String) {
        if (serviceSelected.value != null) {
            database.addService(serviceSelected)
        } else if (serviceNameWI.isNotBlank() && servicePriceWI.isNotBlank()) {
            val newService = Service(id = -System.currentTimeMillis(), serviceName = serviceNameWI, servicePrice = servicePriceWI.toDouble(), serviceDate = null)
            database.addService(newService)
        }
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
        val config = LocalConfiguration.current
        val rounded = String.format(Locale.US,"%.2f", iterable.servicePrice)
        Row(modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = iterable.serviceName,
                modifier = Modifier.weight(1F),
                fontSize = styles.adaptiveSmallFont(config.screenWidthDp))
            Text(text = "$${rounded}",
                modifier = Modifier.weight(1F),
                fontSize = styles.adaptiveSmallFont(config.screenWidthDp))
            button.DatePickerButton { iterable.serviceDate = it
                serviceDateState.value = it }
            input.InputFieldSmall(
                serviceDateState.value,
                { serviceDateState.value = it
                    iterable.serviceDate = it }, "Date")
            button.RemoveButton { database.removeService(iterable.id) }
        }
    }
}



