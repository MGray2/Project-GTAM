package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.platform.LocalContext
import com.example.gtam.ui.theme.Components


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                MainMenu()
            }
        }
    }
}

@Composable
fun MainMenu() {
    val context = LocalContext.current
    val component = Components()
    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
    component.MainMenuButton(context, Activity1::class.java, "Main Account Setup")
    component.MainMenuButton(context, Activity2::class.java, "Manage Client Info")
    }

}
