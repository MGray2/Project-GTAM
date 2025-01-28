package com.example.gtam


import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import com.example.gtam.ui.theme.Components
import com.example.gtam.ui.theme.GTAMTheme
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
                    component.CustomHeader("Main Account Setup")
                    // Bot Email
                    LittleTextWIButton("Bot Email", component, message1)
                    component.InputField("Your Email Here")
                    // Bot Phone Number
                    LittleTextWIButton("Bot Phone Number", component, message2)
                    component.InputField("Your Phone Number Here")
                    // Message Header
                    LittleTextWIButton("Message Header", component, message3)
                    component.InputFieldLarge("Message")
                    // Message Footer
                    LittleTextWIButton("Message Footer", component, message4)
                    component.InputFieldLarge("Message")
                }

            }
        }
    }
}

@Composable
private fun LittleText(text: String, modifier: Modifier) {
    Text(text, fontSize = 22.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.wrapContentWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            )

}

@Composable
private fun LittleTextWIButton(text: String, component: Components, message: String) {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        LittleText(text, Modifier.weight(1F))
        component.InfoButton(message)
    }
}


