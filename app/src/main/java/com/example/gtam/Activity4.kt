package com.example.gtam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme

class Activity4 : ComponentActivity() {
    val component = Components()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                Column() {
                    component.CustomHeader("Compose Message")
                    component.InputDropDown()
                }

            }
        }
    }
}