package com.example.gtam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import com.example.gtam.ui.theme.GTAMTheme

class Activity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                Text("Welcome to Activity 2")
            }
        }
    }
}