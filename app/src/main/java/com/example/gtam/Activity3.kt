package com.example.gtam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.Service
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Manage Services
class Activity3 : ComponentActivity() {
    private val component = Components()
    private val message1 = "Write the name of your service to be selected at message composition."
    private val message2 = "This is the cost of your service, you can also add the same name with a different price."
    private val dbServices: ActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var service by remember { mutableStateOf("") }
            var price by remember { mutableStateOf("") }
            val serviceList by dbServices.allServices.observeAsState(initial = emptyList())

            GTAMTheme {
                Column(modifier = Modifier) {


                    component.CustomHeader("Manage Services")
                    component.LittleTextWIButton("Service", message1)
                    component.InputField(service, { service = it }, "Service")
                    component.LittleTextWIButton("Price", message2)
                    component.InputFieldNumber(price, { price = it }, "Price", KeyboardType.Decimal)
                    component.ButtonGeneric({
                        dbServices.insertService(service, price.toDouble())
                        service = ""
                        price = "" }, "Add Service")
                    // Window to view services
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                            .fillMaxWidth().padding(10.dp)
                    ) {
                        serviceList.forEach { service ->
                            Text(text = "${service.serviceName} - $${service.servicePrice}") // Example display
                        }
                    }
                }
            }
        }
    }
}

private fun doNothing() {
    return
}

class ActivityViewModel : ViewModel() {
    private val serviceDAO = MyApp.database.serviceDao()

    val allServices: LiveData<List<Service>> = serviceDAO.getAllServices().asLiveData()

    fun insertService(serviceName: String, servicePrice: Double) {
        viewModelScope.launch {
            val newService = Service(serviceName = serviceName, servicePrice = servicePrice)
        serviceDAO.insertService(newService)
        }

    }
}