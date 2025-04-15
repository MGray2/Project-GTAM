package com.example.gtam.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Orange60,
    onPrimary = Color.White,
    secondary = Orange85,
    onSecondary = Color.Black,
    tertiary = Orange149,
    onTertiary = Color.Black,
    background = Gray38,
    onBackground = Gray230,
    surface = Gray64,
    onSurface = Gray230
)

private val LightColorScheme = lightColorScheme(
    primary = Green240,
    onPrimary = Color.White,
    secondary = Green194,
    onSecondary = Color.Black,
    tertiary = Green255,
    onTertiary = Color.Black,
    background = Gray242,
    onBackground = Color.Black,
    surface = Gray217,
    onSurface = Color.Black
)

@Composable
fun GTAMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}