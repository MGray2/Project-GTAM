package com.example.gtam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    component.CustomHeader("Manage Clients")
                    component.LittleText("Client's Email", modifier = Modifier)
                    component.InputField("Email")
                    component.LittleText("Client's Phone Number", modifier = Modifier)
                    component.InputFieldNumber("Phone Number")
                    component.ButtonGeneric({ doNothing() }, "Save")
                }

            }
        }
    }
}

fun doNothing() {
    return
}