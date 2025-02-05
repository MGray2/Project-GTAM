package com.example.gtam

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.Components

class Activity2 : ComponentActivity() {
    private val component = Components()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                Column (modifier = Modifier) {
                    var clientEmail by remember { mutableStateOf("") }
                    var clientPhoneNumber by remember { mutableStateOf("") }

                    component.CustomHeader("Manage Clients")
                    component.LittleText("Client's Email", modifier = Modifier)
                    component.InputField(clientEmail,{ clientEmail = it },"Email")
                    component.LittleText("Client's Phone Number", modifier = Modifier)
                    component.InputFieldNumber(clientPhoneNumber,{ clientPhoneNumber = it },"Phone Number", KeyboardType.Number)
                    component.ButtonGeneric({ dataTest(clientPhoneNumber) }, "Save")
                }

            }
        }
    }
}

private fun doNothing() {
    return
}

private fun dataTest(input: String) {
    Log.d("DataTest", "From input: $input")
}