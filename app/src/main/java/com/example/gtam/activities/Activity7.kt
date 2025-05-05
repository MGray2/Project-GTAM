package com.example.gtam.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.gtam.MyApp
import com.example.gtam.Strings
import com.example.gtam.database.entities.UserBot
import com.example.gtam.database.factory.UserBotFactory
import com.example.gtam.database.viewmodel.BotViewModel
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.components.Banners
import com.example.gtam.ui.theme.components.Buttons
import com.example.gtam.ui.theme.components.Input
import com.example.gtam.ui.theme.components.Styles

// Bot Account Setup
class Activity7 : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val input = Input(Styles())
    private val button = Buttons(Styles())
    private val userBotVM: BotViewModel by viewModels { UserBotFactory(MyApp.userBotRepository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val context = LocalContext.current
            var botEmail by remember { mutableStateOf("") }
            var botMJApiKey by remember { mutableStateOf("") }
            var botMJSecretKey by remember { mutableStateOf("") }
            var botNVApiKey by remember { mutableStateOf("") }
            var hideInfo by remember { mutableStateOf(true) }
            // Database
            val bot by userBotVM.userBot.observeAsState(initial = UserBot(
                id = 1,
                email = null,
                mjApiKey = null,
                mjSecretKey = null,
                nvApiKey = null,
                emailSubject = "",
                emailHeader = "",
                emailFooter = "",
                textHeader = "",
                textFooter = ""
            ))
            LaunchedEffect(bot) {
                // Populate the fields
                bot.email?.let { botEmail = it }
                bot.mjApiKey?.let { botMJApiKey = it }
                bot.mjSecretKey?.let { botMJSecretKey = it }
                bot.nvApiKey?.let { botNVApiKey = it }
            }
            GTAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        banner.CustomHeader("Bot Account Setup")
                        // Bot Email
                        banner.LittleText("Email", modifier = Modifier, button, Strings.A7M1)
                        input.InputField(botEmail, { botEmail = it },"Email")
                        // API
                        banner.LittleText("Api Setup", modifier = Modifier, button, Strings.A7M2)
                        input.InputFieldSecure(botMJApiKey, { botMJApiKey = it}, "Mailjet Api Key", hideInfo)
                        input.InputFieldSecure(botMJSecretKey, { botMJSecretKey = it }, "Mailjet Secret Key", hideInfo)
                        input.InputFieldSecure(botNVApiKey, { botNVApiKey = it },"NumVerify Api Key", hideInfo)
                        input.InputSwitch(hideInfo, { hideInfo = it }, "Hide Api Information")
                        // Save Button
                        button.ButtonGeneric({
                            userBotVM.updateBotInfo(botEmail, botMJApiKey, botMJSecretKey, botNVApiKey)
                            button.showToast("Information Saved", context)
                        }, "Save")
                    }
                }
            }
        }
    }
}