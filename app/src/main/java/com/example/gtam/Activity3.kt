package com.example.gtam


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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gtam.database.Service
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import com.example.gtam.viewmodel.ServiceViewModel

// Manage Services
class Activity3 : ComponentActivity() {
    // Global
    private val component = Components()
    private val message1 = "Write the name of your service to be selected at message composition."
    private val message2 = "This is the cost of your service, you can also add the same name with a different price."
    private val dbServices: ServiceViewModel by viewModels()

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
                Column(modifier = Modifier) {
                    component.CustomHeader("Manage Services")
                    // Service Name
                    component.LittleTextWIButton("Service", message1)
                    component.InputField(service, { service = it }, "Service")
                    // Service Price
                    component.LittleTextWIButton("Price", message2)
                    component.InputFieldNumber(price, { price = it }, "Price", KeyboardType.Decimal)
                    // Save Button
                    component.ButtonGeneric({
                        if (service != "" && price != "") {
                            dbServices.insertService(service, price.toDouble())
                            service = ""
                            price = "" }
                        else { component.ShowToast("All fields must be filled.", context) } }, "Add Service")
                    // Window to view services
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                            .fillMaxWidth().padding(10.dp)
                    ) {
                        var itemCount = 0
                        serviceList.forEach { service ->
                            ServiceWindow(itemCount, service, dbServices, component)
                            itemCount++
                        }
                    }
                }
            }
        }
    }
}

// Window to view the list of Services
@Composable
private fun ServiceWindow(counter: Int, iterable: Service, database: ServiceViewModel, component: Components) {
    if (counter % 2 != 0) {
        ServiceRow(modifier = Modifier.background(Color.LightGray), iterable, database, component)
    } else {
        ServiceRow(modifier = Modifier, iterable, database, component)
    }
}

// Additional styling for each Service
@Composable
private fun ServiceRow(modifier: Modifier, iterable: Service, database: ServiceViewModel, component: Components) {
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
        component.DeleteButton { database.deleteService(iterable.id) }
    }
}