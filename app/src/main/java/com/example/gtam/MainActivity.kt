package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.platform.LocalContext
import com.example.gtam.ui.theme.components.*


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
    val banner = Banners()
    val button = Buttons()
    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        button.MainMenuButton(context, Activity4::class.java, "Compose Message")
        banner.LittleText("Setup", modifier = Modifier)
        button.MainMenuButton(context, Activity1::class.java, "Bot Account Setup")
        button.MainMenuButton(context, Activity2::class.java, "Manage Client Info")
        button.MainMenuButton(context, Activity3::class.java, "Manage Services")
    }

}
