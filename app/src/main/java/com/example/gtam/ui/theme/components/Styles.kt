package com.example.gtam.ui.theme.components


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
            screenWidth < 600 -> 38.sp
            screenWidth < 840 -> 42.sp
            else -> 46.sp
        }
    }

    fun adaptiveSmallHeight(screenHeight: Int): Dp {
        return when {
            screenHeight < 600 -> 70.dp
            screenHeight < 840 -> 73.dp
            else -> 78.dp
        }
    }

    fun adaptiveLargeHeight(screenHeight: Int): Dp {
        return when {
            screenHeight < 600 -> 130.dp
            screenHeight < 840 -> 140.dp
            else -> 170.dp
        }
    }

    fun adaptiveBannerHeight(screenHeight: Int): Dp {
        return when {
            screenHeight < 600 -> 120.dp
            screenHeight < 840 -> 130.dp
            else -> 140.dp
        }
    }

    fun adaptiveSmallWidth(screenWidth: Int): Dp {
        return when {
            screenWidth < 600 -> 160.dp
            screenWidth < 840 -> 180.dp
            else -> 200.dp
        }
    }

}