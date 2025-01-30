package com.example.gtam


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.Modifier


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
                    component.CustomHeader("Bot Account Setup")
                    // Bot Email
                    component.LittleTextWIButton("Bot Email", component, message1)
                    component.InputField("Your Email Here")
                    // Bot Phone Number
                    component.LittleTextWIButton("Bot Phone Number", component, message2)
                    component.InputFieldNumber("Your Phone Number Here")
                    // Message Header
                    component.LittleTextWIButton("Message Header", component, message3)
                    component.InputFieldLarge("Message")
                    // Message Footer
                    component.LittleTextWIButton("Message Footer", component, message4)
                    component.InputFieldLarge("Message")
                }

            }
        }
    }
}




