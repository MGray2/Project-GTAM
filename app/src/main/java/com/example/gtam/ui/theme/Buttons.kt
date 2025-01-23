package com.example.gtam.ui.theme

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class Buttons {

    @Composable
    fun MainMenuButton(context: Context, activityClass: Class<out Activity>, text: String) {
        Button(
            onClick = {
                val intent = Intent(context, activityClass)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.hsl(120.0F, 1.0F, 0.3F),
                contentColor = Color.White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 0.dp, vertical = 10.dp)

        ) {
            Text(text, fontSize = 30.sp)
        }
    }
}