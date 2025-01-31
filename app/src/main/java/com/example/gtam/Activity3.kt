package com.example.gtam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme

class Activity3 : ComponentActivity() {
    private val component = Components()
    private val message1 = "Write the name of your service to be selected at message composition."
    private val message2 = "This is the cost of your service, you can also add the same name with a different price."
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                Column(modifier = Modifier) {
                    var service by remember { mutableStateOf("") }
                    var price by remember { mutableStateOf("") }

                    component.CustomHeader("Manage Services")
                    component.LittleTextWIButton("Service", message1)
                    component.InputField(service, { service = it }, "Service")
                    component.LittleTextWIButton("Price", message2)
                    component.InputField(price, { price = it }, "Price")
                    component.ButtonGeneric({ doNothing() }, "Add Service")
                    // Window to view services
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                            .fillMaxWidth().padding(10.dp)
                    ) {
                        // list goes here when ready
                    }
                }

            }
        }
    }
}

private fun doNothing() {
    return
}