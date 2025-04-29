package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.platform.LocalContext
import com.example.gtam.ui.theme.components.*


class MainActivity : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val button = Buttons(Styles())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val context = LocalContext.current
            // UI
            GTAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        banner.MainHeader()
                        banner.LittleText("Compose", modifier = Modifier)
                        button.MainMenuButton(context, Activity4::class.java, "Compose Message")
                        banner.LittleText("Setup", modifier = Modifier)
                        button.MainMenuButton(context, Activity1::class.java, "Message Defaults")
                        button.MainMenuButton(context, Activity2::class.java, "Manage Client Info")
                        button.MainMenuButton(context, Activity3::class.java, "Manage Services")
                        button.MainMenuButton(context, Activity7::class.java, "Bot Account Setup")
                        banner.LittleText("Misc", modifier = Modifier)
                        button.MainMenuButton(context, Activity5::class.java, "Message History")
                    }
                }
            }
        }
    }
}

