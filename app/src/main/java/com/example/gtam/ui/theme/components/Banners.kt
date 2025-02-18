package com.example.gtam.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gtam.ui.theme.Green240
import com.example.gtam.ui.theme.Green255

class Banners {

    // Colored header for activities
    @Composable
    fun CustomHeader(text: String) {
        val bannerColors = listOf(Green240, Green255)
        Text(text, fontSize = 34.sp, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        bannerColors,
                        start = Offset(0.0F, 0.0F),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ))
                .padding(0.dp, 30.dp, 0.dp, 20.dp))
    }

    // Little text banner
    @Composable
    fun LittleText(text: String, modifier: Modifier) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
                    .padding(0.dp, 15.dp, 0.dp, 0.dp)
            )
        }
    }

    // Little text banner with an info button with a popup
    @Composable
    fun LittleText(text: String, modifier: Modifier, button: Buttons, message: String) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(text, fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
                    .padding(0.dp, 15.dp, 0.dp, 0.dp)
            )
            button.InfoButton(message)
        }
    }
}