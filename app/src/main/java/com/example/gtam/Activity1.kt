package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


class Activity1 : ComponentActivity()
{
    private val component = Components()
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
                    component.ButtonGeneric({ doNothing() }, "Save")
                }

            }
        }
    }
}

private fun doNothing() {
    return
}


