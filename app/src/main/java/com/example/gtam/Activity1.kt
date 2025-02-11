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
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.UserBot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

// Bot Account Setup
class Activity1 : ComponentActivity()
{
    private val component = Components()
    private val dbBot: BotViewModel by viewModels()
    private val message1 = "This will be the email that the system uses for messaging."
    private val message2 = "This will be the phone number that the system uses for texting."
    private val message3 = "The system will default to this starting message should no additional input be included."
    private val message4 = "The system will default to this closing message should no additional input be included."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                Column(modifier = Modifier) {
                    var botGmail by remember { mutableStateOf("") }
                    var botOutlook by remember { mutableStateOf("") }
                    var botPhoneNumber by remember { mutableStateOf("") }
                    var messageHeader by remember { mutableStateOf("") }
                    var messageFooter by remember { mutableStateOf("") }
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

                    component.CustomHeader("Bot Account Setup")
                    // Bot Email
                    component.LittleTextWIButton("Bot Email", message1)
                    component.InputField(botGmail, { botGmail = it },"Gmail")
                    component.InputField(botOutlook, { botOutlook = it }, placeholder = "Outlook")
                    // Bot Phone Number
                    component.LittleTextWIButton("Bot Phone Number", message2)
                    component.InputFieldNumber(botPhoneNumber, { botPhoneNumber = it },"Phone Number", KeyboardType.Number)
                    // Message Header
                    component.LittleTextWIButton("Message Header", message3)
                    component.InputFieldLarge(messageHeader, { messageHeader = it },"Message")
                    // Message Footer
                    component.LittleTextWIButton("Message Footer", message4)
                    component.InputFieldLarge(messageFooter, { messageFooter = it },"Message")
                    // Save Button
                    component.ButtonGeneric({ dbBot.updateBot(botGmail, botOutlook, botPhoneNumber, messageHeader, messageFooter) }, "Save")
                }
            }
        }
    }
}

class BotViewModel : ViewModel() {
    private val botDAO = MyApp.database.userBotDao()

    private val _userBot = MutableLiveData<UserBot>()
    val userBot: LiveData<UserBot> get() = _userBot

    init {
        viewModelScope.launch {
            _userBot.value = getOrCreateBot()
        }
    }

    // Find instance of UserBot or else create one
    private suspend fun getOrCreateBot(): UserBot {
        val existingBot = botDAO.getUserBot().firstOrNull()
        return if (existingBot != null) {
            existingBot
        } else {
            val defaultBot = UserBot(
                id = 1,
                gmail = null,
                outlook = null,
                phoneNumber = null,
                messageHeader = "",
                messageFooter = ""
            )
            botDAO.insertOrUpdate(defaultBot)
            defaultBot
        }
    }

    // Update changes to UserBot
    fun updateBot(gmail: String, outlook: String, phoneNumber: String, messageHeader: String, messageFooter: String) {
        viewModelScope.launch {
            botDAO.updateUserBot(gmail = gmail,
                outlook = outlook,
                phoneNumber = phoneNumber,
                messageHeader = messageHeader,
                messageFooter = messageFooter)
        }
    }
}


