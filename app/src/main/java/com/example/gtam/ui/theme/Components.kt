package com.example.gtam.ui.theme

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gtam.R


class Components {

    @Composable
    fun MainMenuButton(context: Context, activityClass: Class<out Activity>, text: String) {
        Button(
            onClick = {
                val intent = Intent(context, activityClass)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.hsl(120.0F, 1.0F, 0.37F),
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

    @Composable
    fun InputField(placeholder: String) {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(placeholder, fontSize = 30.sp) },
            modifier = Modifier.padding(10.dp)
                .fillMaxWidth()
                .height(70.dp)
        )
    }

    @Composable
    fun InputFieldLarge(placeholder: String) {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(placeholder, fontSize = 24.sp) },
            modifier = Modifier.padding(10.dp)
                .fillMaxWidth()
                .height(120.dp)
        )
    }

    @Composable
    fun InfoButton(message: String)
    {
        var showDialog by remember { mutableStateOf(false) }
        Button(onClick = { showDialog = true },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.DarkGray
            )
        ) {
            Icon(painter = painterResource(id = R.drawable.info),
                contentDescription = "Info",
                modifier = Modifier.size(20.dp))
        }

        if (showDialog) {
            CustomMessageBox(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                message = message
            )
        }
    }

    @Composable
    fun CustomMessageBox(showDialog: Boolean, onDismiss: () -> Unit, message: String) {
        if (showDialog) {
            Dialog(onDismissRequest = { onDismiss() }) {
                Box(
                    modifier = Modifier
                        .size(300.dp, 200.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }

}