package com.example.gtam


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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gtam.database.entities.Service
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.platform.LocalContext
import com.example.gtam.database.factory.ServiceFactory
import java.util.Locale
import com.example.gtam.database.viewmodel.ServiceViewModel

// Manage Services
class Activity3 : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val button = Buttons(Styles())
    private val input = Input(Styles())
    private val messages = Strings()
    private val dbServices: ServiceViewModel by viewModels { ServiceFactory(MyApp.serviceRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val context = LocalContext.current
            var service by remember { mutableStateOf("") }
            var price by remember { mutableStateOf("") }
            // Database
            val serviceList by dbServices.allServices.observeAsState(initial = emptyList())

            // UI
            GTAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        banner.CustomHeader("Manage Services")
                        // Service Name
                        banner.LittleText("Service", modifier = Modifier, button, messages.a3m1)
                        input.InputField(service, { service = it }, "Service")
                        // Service Price
                        banner.LittleText("Price", modifier = Modifier, button, messages.a3m2)
                        input.InputField(price, { price = it }, "Price", KeyboardType.Decimal)
                        // Save Button
                        button.ButtonGeneric({
                            if (service != "" && price != "") {
                                dbServices.insertService(service, price.toDouble())
                                service = ""
                                price = "" }
                            else { button.showToast("All fields must be filled.", context) } }, "Add Service")
                        // Window to view services
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            var itemCount = 0
                            serviceList.forEach { service ->
                                ServiceWindow(itemCount, service, dbServices, button)
                                itemCount++
                            }
                        }
                    }
                }
            }
        }
    }
}

// Window to view the list of Services
@Composable
private fun ServiceWindow(counter: Int, iterable: Service, database: ServiceViewModel, button: Buttons) {
    if (counter % 2 != 0) {
        ServiceRow(modifier = Modifier.background(MaterialTheme.colorScheme.surface), iterable, database, button)
    } else {
        ServiceRow(modifier = Modifier, iterable, database, button)
    }
}

// Additional styling for each Service
@Composable
private fun ServiceRow(modifier: Modifier, iterable: Service, database: ServiceViewModel, button: Buttons) {
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
        button.DeleteButton { database.deleteService(iterable.id) }
    }
}