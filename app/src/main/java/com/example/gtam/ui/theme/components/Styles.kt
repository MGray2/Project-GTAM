package com.example.gtam.ui.theme.components

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Class that holds style rules
class Styles() {

    fun adaptiveSmallFont(screenWidth: Int): TextUnit {
        return when {
            screenWidth < 600 -> 20.sp
            screenWidth < 840 -> 28.sp
            else -> 32.sp
        }
    }

    fun adaptiveMediumFont(screenWidth: Int): TextUnit {
        return when {
            screenWidth < 600 -> 26.sp
            screenWidth < 840 -> 30.sp
            else -> 34.sp
        }
    }

    fun adaptiveLargeFont(screenWidth: Int): TextUnit {
        return when {
            screenWidth < 600 -> 30.sp
            screenWidth < 840 -> 34.sp
            else -> 38.sp
        }
    }

    fun adaptiveBannerFont(screenWidth: Int): TextUnit {
        return when {
            screenWidth < 600 -> 34.sp
            screenWidth < 840 -> 38.sp
            else -> 42.sp
        }
    }

    fun adaptiveSmallHeight(screenHeight: Int): Dp {
        return when {
            screenHeight < 600 -> 70.dp
            screenHeight < 840 -> 80.dp
            else -> 90.dp
        }
    }

    fun adaptiveLargeHeight(screenHeight: Int): Dp {
        return when {
            screenHeight < 600 -> 120.dp
            screenHeight < 840 -> 130.dp
            else -> 140.dp
        }
    }

}