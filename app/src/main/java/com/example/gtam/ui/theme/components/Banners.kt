package com.example.gtam.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gtam.ui.theme.Green168
import com.example.gtam.ui.theme.Green194
import com.example.gtam.ui.theme.Green240
import com.example.gtam.ui.theme.Green255
import com.example.gtam.ui.theme.Orange50
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

// Class that holds banner displays and text boxes
class Banners(private val styles: Styles) {

    @Composable
    fun MainHeader() {
        val bannerColors = listOf(Green194, Green240)
        Column (modifier = Modifier.fillMaxWidth()
            .background(
                Brush.linearGradient(
                    bannerColors,
                    start = Offset(Float.MIN_VALUE, Float.MIN_VALUE),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ))) {
            Text("Green Team", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 5.dp).fillMaxWidth(),
                style = TextStyle(
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(8F, 8F)
                    )
                )
            )
            Text("Auto Messenger", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 25.dp).fillMaxWidth(),
                style = TextStyle(
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(8F, 8F)
                    )
                )
            )
        }
    }

    // Colored header for activities
    @Composable
    fun CustomHeader(text: String) {
        val config = LocalConfiguration.current
        val bannerColors = listOf(Green240, Green255)

        Text(text,
            fontSize = styles.adaptiveBannerFont(config.screenWidthDp),
            textAlign = TextAlign.Center,
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
        val configuration = LocalConfiguration.current

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = styles.adaptiveSmallFont(configuration.screenWidthDp),
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
                    .padding(0.dp, 15.dp, 0.dp, 0.dp)
            )
        }
    }

    // Little text banner with an info button with a popup
    @Composable
    fun LittleText(text: String, modifier: Modifier, button: Buttons, message: String) {
        val configuration = LocalConfiguration.current

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(text, fontSize = styles.adaptiveSmallFont(configuration.screenWidthDp),
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
                    .padding(0.dp, 15.dp, 0.dp, 0.dp)
            )
            button.InfoButton(message)
        }
    }
}