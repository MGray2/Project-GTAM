package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gtam.ui.theme.components.*
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.gtam.database.UserBot
import com.example.gtam.viewmodel.BotViewModel

// Bot Account Setup
class Activity1 : ComponentActivity() {
    // Global
    private val input = Input()
    private val button = Buttons()
    private val banner = Banners()
    private val dbBot: BotViewModel by viewModels()
    private val message1 = "This will be the email that the system uses for messaging."
    private val message2 = "This will be the phone number that the system uses for texting."
    private val message3 = "The system will default to this starting message should no additional input be included."
    private val message4 = "The system will default to this closing message should no additional input be included."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            var botGmail by remember { mutableStateOf("") }
            var botOutlook by remember { mutableStateOf("") }
            var botPhoneNumber by remember { mutableStateOf("") }
            var messageHeader by remember { mutableStateOf("") }
            var messageFooter by remember { mutableStateOf("") }
            // Database
            val bot by dbBot.userBot.observeAsState(initial = UserBot(id = 1, gmail = "", outlook = "", phoneNumber = "", messageHeader = "", messageFooter = ""))
            if (bot.gmail != null) {
                botGmail = bot.gmail!!
            }
            if (bot.outlook != null) {
                botOutlook = bot.outlook!!
            }
            if (bot.phoneNumber != null) {
                botPhoneNumber = bot.phoneNumber!!
            }
            messageHeader = bot.messageHeader
            messageFooter = bot.messageFooter

            // UI
            GTAMTheme {
                Column(modifier = Modifier) {
                    banner.CustomHeader("Bot Account Setup")
                    // Bot Email
                    banner.LittleText("Bot Email", modifier = Modifier, button, message1)
                    input.InputField(botGmail, { botGmail = it },"Gmail")
                    input.InputField(botOutlook, { botOutlook = it }, placeholder = "Outlook")
                    // Bot Phone Number
                    banner.LittleText("Bot Phone Number", modifier = Modifier, button, message2)
                    input.InputFieldNumber(botPhoneNumber, { botPhoneNumber = it },"Phone Number", KeyboardType.Number)
                    // Message Header
                    banner.LittleText("Message Header", modifier = Modifier, button, message3)
                    input.InputFieldLarge(messageHeader, { messageHeader = it },"Message")
                    // Message Footer
                    banner.LittleText("Message Footer", modifier = Modifier, button, message4)
                    input.InputFieldLarge(messageFooter, { messageFooter = it },"Message")
                    // Save Button
                    button.ButtonGeneric({ dbBot.updateBot(botGmail, botOutlook, botPhoneNumber, messageHeader, messageFooter) }, "Save")
                }
            }
        }
    }
}




