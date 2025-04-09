package com.example.gtam.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
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
import com.example.gtam.ui.theme.Green194
import com.example.gtam.ui.theme.Green240
import com.example.gtam.ui.theme.Green255

// Class that holds banner displays and text boxes
class Banners(private val styles: Styles) {

    @Composable
    fun MainHeader() {
        val config = LocalConfiguration.current
        val bannerColors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary)

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(styles.adaptiveBannerHeight(config.screenHeightDp))
            .background(
                Brush.linearGradient(
                    bannerColors,
                    start = Offset(0.0F, 0.0F),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            ),
            contentAlignment = Alignment.Center) {
            Text("Green Team\nAuto Messenger",
                fontSize = styles.adaptiveBannerFont(config.screenWidthDp),
                textAlign = TextAlign.Center,
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
        val bannerColors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(styles.adaptiveBannerHeight(config.screenHeightDp))
            .background(
                Brush.linearGradient(
                    bannerColors,
                    start = Offset(0.0F, 0.0F),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            ),
            contentAlignment = Alignment.Center)
        {
            Text(text,
                fontSize = styles.adaptiveBannerFont(config.screenWidthDp),
                textAlign = TextAlign.Center,
            )
        }
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