package com.example.gtam

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.gtam.database.entities.Service
import com.example.gtam.database.entities.UserBot
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.database.viewmodel.AllViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Calendar


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
            val bot by dbAll.userBot.observeAsState(initial = UserBot(id = 1, email = null, username = null, password = null, phoneNumber = null, messageHeader = "", messageFooter = ""))
            val clientOptions: LiveData<List<Pair<Long, String>>> = dbAll.clientDropdownList
            val serviceOptions: LiveData<List<Pair<Long, String>>> = dbAll.serviceDropdownList
            val serviceList by dbAll.selectedServices.observeAsState(emptyList())
            // Local
            val context = LocalContext.current
            val clientSelected = remember { mutableStateOf<Long?>(null) }
            val serviceSelected = remember { mutableStateOf<Long?>(null) }
            var messageSubject by remember { mutableStateOf("") }
            var messageHeader by remember { mutableStateOf("") }
            var messageFooter by remember { mutableStateOf("") }
            var serviceNameWI by remember { mutableStateOf("") }
            var servicePriceWI by remember { mutableStateOf("") }
            var rememberThis by remember { mutableStateOf(true) }

            val resetDropdown1 = remember { mutableStateOf<(() -> Unit)?>(null) }
            val resetDropdown2 = remember { mutableStateOf<(() -> Unit)?>(null) }
            messageHeader = bot.messageHeader
            messageFooter = bot.messageFooter
            // UI
            GTAMTheme {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    banner.CustomHeader("Compose Message")
                    // Message Recipient
                    banner.LittleText("Message Recipient", modifier = Modifier)
                    input.InputDropDown(optionsLiveData = clientOptions, selectedId = clientSelected, "Client", { resetDropdown1.value = it })
                    // Subject
                    banner.LittleText("Subject", modifier = Modifier)
                    input.InputField(messageSubject, { messageSubject = it }, "Subject")

                    // Body 1
                    banner.LittleText("Message Header", modifier = Modifier)
                    input.InputFieldLarge(messageHeader, { messageHeader = it }, "Header")

                    // Add Services
                    banner.LittleText("Services", modifier = Modifier)
                    input.InputDropDown(optionsLiveData = serviceOptions, selectedId = serviceSelected, "Add Service", { resetDropdown2.value = it })
                    input.InputField(serviceNameWI, { serviceNameWI = it }, "Write-in Service")
                    input.InputField(servicePriceWI, { servicePriceWI = it }, "Write-in Price")

                    button.ButtonGeneric({
                        saveService(dbAll, serviceSelected, serviceNameWI, servicePriceWI)
                        serviceSelected.value = null
                        serviceNameWI = ""
                        servicePriceWI = ""
                        resetDropdown2.value?.invoke() }, "Add Service")

                    // Service Window
                    Column(modifier = Modifier
                        .fillMaxWidth().padding(10.dp)) {
                        var itemCount = 0
                        serviceList.forEach {
                            service -> ServiceWindow(itemCount, service, dbAll, input, button)
                            itemCount++
                        }
                    }


                    // Body 2
                    banner.LittleText("Message Footer", modifier = Modifier)
                    input.InputFieldLarge(messageFooter, { messageFooter = it }, "Footer")

                    input.InputSwitch(rememberThis, { rememberThis = it }, "Remember this interaction")
                    // Test Button
                    button.ButtonGeneric({
                        sendMessage(dbAll, bot, clientSelected, serviceList, messageSubject, messageHeader, messageFooter)
                        button.showToast("Sending Message", context)
                                         }, "Send")
                }

            }
        }
    }
}

private fun sendMessage(database: AllViewModel, userBot: UserBot, clientSelected: MutableState<Long?>, serviceList: List<Service>, subject: String, header: String, footer: String) {
    CoroutineScope(Dispatchers.IO).launch {
        val clientId = clientSelected.value ?: return@launch
        val client = database.clientById(clientId)

        if (client?.clientEmail.isNullOrBlank()) {
            Log.e("sendMessage", "Client email is null or empty")
            return@launch
        }

        Messenger().sendEmail(
            sender = userBot.email ?: return@launch,
            username = userBot.username ?: return@launch,
            password = userBot.password ?: return@launch,
            recipient = client!!.clientEmail!!,
            sub = subject,
            header = header,
            services = serviceList,
            footer = footer
        )
    }
}

private fun saveService(database: AllViewModel, serviceSelected: MutableState<Long?>, serviceNameWI: String, servicePriceWI: String) {
    if (serviceSelected.value != null) {
        database.addService(serviceSelected)
    } else if (serviceNameWI.isNotBlank() && servicePriceWI.toDouble() > 0) {
        val newService = Service(id = -System.currentTimeMillis(), serviceName = serviceNameWI, servicePrice = servicePriceWI.toDouble(), serviceDate = null)
        database.addService(newService)
    }
}

private fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

// Window to view the list of Services
@Composable
private fun ServiceWindow(counter: Int, iterable: Service, database: AllViewModel, input: Input, button: Buttons) {
    val serviceDateState = remember { mutableStateOf(iterable.serviceDate ?: "") }
    if (counter % 2 != 0) {
        ServiceRow(modifier = Modifier.background(Color.LightGray), iterable, database, input, button, serviceDateState)
    } else {
        ServiceRow(modifier = Modifier, iterable, database, input, button, serviceDateState)
    }
}

// Additional styling for each Service
@Composable
private fun ServiceRow(modifier: Modifier, iterable: Service, database: AllViewModel, input: Input, button: Buttons, serviceDateState: MutableState<String>) {
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
        button.TodayButton { iterable.serviceDate = getTodayDate()
        serviceDateState.value = getTodayDate() }
        input.InputFieldSmall(
            serviceDateState.value,
            { serviceDateState.value = it
            iterable.serviceDate = it }, "Date")
        button.RemoveButton { database.removeService(iterable.id) }
    }
}

