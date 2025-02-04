package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        Image(
            painter = painterResource(id = R.drawable.gtam_screen),
            contentDescription = "GTAM Screen",
            modifier = Modifier.padding(0.dp, 30.dp, 0.dp, 10.dp))
        component.MainMenuButton(context, Activity4::class.java, "Compose Message")
        component.LittleText("Setup", modifier = Modifier)
        component.MainMenuButton(context, Activity1::class.java, "Bot Account Setup")
        component.MainMenuButton(context, Activity2::class.java, "Manage Client Info")
        component.MainMenuButton(context, Activity3::class.java, "Manage Services")
    }

}
