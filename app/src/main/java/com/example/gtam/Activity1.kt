package com.example.gtam


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
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.gtam.database.entities.UserBot
import com.example.gtam.database.factory.UserBotFactory
import com.example.gtam.database.viewmodel.BotViewModel

// Bot Account Setup
class Activity1 : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val input = Input(Styles())
    private val button = Buttons(Styles())
    private val userBotVM: BotViewModel by viewModels { UserBotFactory(MyApp.userBotRepository) }
    private val message1 = "This will be the email that the system uses for messaging."
    private val message2 = "Api keys are needed to perform api calls. \n1: Mailjet Api Key\n2: Mailjet Secret Key\n3: NumVerify Api Key"
    private val message3 = "Default system text for the subject of the email. The subject will be ignored if the message is a text."
    private val message4 = "Default system text for the start of the email, before any listed services."
    private val message5 = "Default system text for the end of the email, after any listed services."

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
            var messageSubject by remember { mutableStateOf("") }
            var messageHeader by remember { mutableStateOf("") }
            var messageFooter by remember { mutableStateOf("") }
            var hideInfo by remember { mutableStateOf(true) }
            // Database
            val bot by userBotVM.userBot.observeAsState(initial = UserBot(
                id = 1,
                email = null,
                mjApiKey = null,
                mjSecretKey = null,
                nvApiKey = null,
                messageSubject = "",
                messageHeader = "",
                messageFooter = ""
            ))
            LaunchedEffect(bot) {
                // Populate the fields
                bot.email?.let { botEmail = it }
                bot.mjApiKey?.let { botMJApiKey = it }
                bot.mjSecretKey?.let { botMJSecretKey = it }
                bot.nvApiKey?.let { botNVApiKey = it }

                messageSubject = bot.messageSubject
                messageHeader = bot.messageHeader
                messageFooter = bot.messageFooter
            }

            // UI
            GTAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        banner.CustomHeader("Bot Account Setup")
                        // Bot Email
                        banner.LittleText("Email", modifier = Modifier, button, message1)
                        input.InputField(botEmail, { botEmail = it },"Email")
                        // API
                        banner.LittleText("Api Setup", modifier = Modifier, button, message2)
                        input.InputFieldSecure(botMJApiKey, { botMJApiKey = it}, "Mailjet Api Key", hideInfo)
                        input.InputFieldSecure(botMJSecretKey, { botMJSecretKey = it }, "Mailjet Secret Key", hideInfo)
                        input.InputFieldSecure(botNVApiKey, { botNVApiKey = it },"NumVerify Api Key", hideInfo)
                        input.InputSwitch(hideInfo, { hideInfo = it }, "Hide Api Information")
                        // Message Subject
                        banner.LittleText("Subject", modifier = Modifier, button, message3)
                        input.InputField(messageSubject, { messageSubject = it }, "Subject")
                        // Message Header
                        banner.LittleText("Message Header", modifier = Modifier, button, message4)
                        input.InputFieldLarge(messageHeader, { messageHeader = it },"Message")
                        // Message Footer
                        banner.LittleText("Message Footer", modifier = Modifier, button, message5)
                        input.InputFieldLarge(messageFooter, { messageFooter = it },"Message")
                        // Save Button
                        button.ButtonGeneric({
                            userBotVM.updateBot(botEmail, botMJApiKey, botMJSecretKey, botNVApiKey, messageSubject, messageHeader, messageFooter)
                            button.showToast("Account Saved", context)
                        }, "Save")
                    }
                }
            }
        }
    }
}




