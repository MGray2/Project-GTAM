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

// Message Defaults
class Activity1 : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val input = Input(Styles())
    private val button = Buttons(Styles())
    private val messages = Strings
    private val userBotVM: BotViewModel by viewModels { UserBotFactory(MyApp.userBotRepository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val context = LocalContext.current
            var emailSubject by remember { mutableStateOf("") }
            var emailHeader by remember { mutableStateOf("") }
            var emailFooter by remember { mutableStateOf("") }
            var textHeader by remember { mutableStateOf("") }
            var textFooter by remember { mutableStateOf("") }
            var textSwitch by remember { mutableStateOf(false) }
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
                emailSubject = bot.emailSubject.ifBlank { messages.a1p1 }
                emailHeader = bot.emailHeader.ifBlank { messages.a1p2 }
                emailFooter = bot.emailFooter.ifBlank { messages.a1p3 }
                textHeader = bot.textHeader.ifBlank { messages.a1p4 }
                textFooter = bot.textFooter.ifBlank { messages.a1p5 }
            }

            // UI
            GTAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        banner.CustomHeader("Message Defaults")
                        input.InputSwitch(textSwitch, {textSwitch = it}, "Text Specific Message")
                        if (!textSwitch) {
                            // Message Subject
                            banner.LittleText("Message Subject", modifier = Modifier, button, messages.a1m1)
                            input.InputField(emailSubject, { emailSubject = it }, "Subject")
                            // Message Header
                            banner.LittleText("Message Header", modifier = Modifier, button, messages.a1m2)
                            input.InputFieldLarge(emailHeader, { emailHeader = it },"Message")
                            // Message Footer
                            banner.LittleText("Message Footer", modifier = Modifier, button, messages.a1m3)
                            input.InputFieldLarge(emailFooter, { emailFooter = it },"Message")
                        } else {
                            // Message Header (text)
                            banner.LittleText("Message Header", modifier = Modifier, button, messages.a1m2)
                            input.InputFieldLarge(textHeader, { textHeader = it },"Message")
                            // Message Footer (text)
                            banner.LittleText("Message Footer", modifier = Modifier, button, messages.a1m3)
                            input.InputFieldLarge(textFooter, { textFooter = it },"Message")
                        }

                        // Save Button
                        button.ButtonGeneric({
                            userBotVM.updateBotDefaults(
                                emailSubject,
                                emailHeader,
                                emailFooter,
                                textHeader,
                                textFooter
                            )
                            button.showToast("Information Saved", context)
                        }, "Save")
                    }
                }
            }
        }
    }
}




