package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.gtam.database.entities.UserBot
import com.example.gtam.database.factory.UserBotFactory
import com.example.gtam.database.viewmodel.BotViewModel

// Bot Account Setup
class Activity1 : ComponentActivity() {
    // Global
    private val input = Input()
    private val button = Buttons()
    private val banner = Banners()
    private val dbBot: BotViewModel by viewModels { UserBotFactory(MyApp.userBotRepository) }
    private val message1 = "This will be the email that the system uses for messaging."
    private val message2 = "This will be the phone number that the system uses for texting."
    private val message3 = "The system will default to this starting message should no additional input be included."
    private val message4 = "The system will default to this closing message should no additional input be included."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            var botEmail by remember { mutableStateOf("") }
            var botUsername by remember { mutableStateOf("") }
            var botPassword by remember { mutableStateOf("") }
            var botPhoneNumber by remember { mutableStateOf("") }
            var messageSubject by remember { mutableStateOf("") }
            var messageHeader by remember { mutableStateOf("") }
            var messageFooter by remember { mutableStateOf("") }
            // Database
            val bot by dbBot.userBot.observeAsState(initial = UserBot(id = 1, email = null, username = null, password = null, phoneNumber = null, messageHeader = "", messageFooter = ""))
            if (bot.email != null) {
                botEmail = bot.email!!
            }
            if (bot.username != null) {
                botUsername = bot.username!!
            }
            if (bot.password != null) {
                botPassword = bot.password!!
            }
            if (bot.phoneNumber != null) {
                botPhoneNumber = bot.phoneNumber!!
            }
            messageHeader = bot.messageHeader
            messageFooter = bot.messageFooter

            // UI
            GTAMTheme {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    banner.CustomHeader("Bot Account Setup")
                    // Bot Email
                    banner.LittleText("Email", modifier = Modifier, button, message1)
                    input.InputField(botEmail, { botEmail = it },"Email")
                    // API
                    banner.LittleText("Api Setup", modifier = Modifier, button, message2)
                    input.InputField(botUsername, { botUsername = it}, "Api Key")
                    input.InputField(botPassword, { botPassword = it }, placeholder = "Api Secret Key")

                    // Bot Phone Number
                    banner.LittleText("Bot Phone Number", modifier = Modifier, button, message2)
                    input.InputFieldNumber(botPhoneNumber, { botPhoneNumber = it },"Phone Number", KeyboardType.Number)
                    // Message Header
                    banner.LittleText("Subject", modifier = Modifier)
                    input.InputField(messageSubject, { messageSubject = it }, "Subject")

                    banner.LittleText("Message Header", modifier = Modifier, button, message3)
                    input.InputFieldLarge(messageHeader, { messageHeader = it },"Message")
                    // Message Footer
                    banner.LittleText("Message Footer", modifier = Modifier, button, message4)
                    input.InputFieldLarge(messageFooter, { messageFooter = it },"Message")
                    // Save Button
                    button.ButtonGeneric({ dbBot.updateBot(botEmail, botUsername, botPassword, botPhoneNumber, messageHeader, messageFooter) }, "Save")
                }
            }
        }
    }
}




