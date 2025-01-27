package com.example.gtam


import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class Activity1 : ComponentActivity()
{
    private val component = Components()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GTAMTheme {
                Column(modifier = Modifier) {
                    component.CustomHeader("Main Account Setup")
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.weight(1F))
                        LittleText("Bot Email")
                        component.InfoButton()
                    }
                    component.InputField("Your Email Here")
                    LittleText("Bot Phone Number")
                    component.InputField("Your Phone Number Here")
                    LittleText("Message Header")
                    component.InputFieldLarge("Message")
                    LittleText("Message Footer")
                    component.InputFieldLarge("Message")
                }

            }
        }
    }
}

@Composable
private fun LittleText(text: String) {
    Text(text, fontSize = 22.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.wrapContentWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            )

}


