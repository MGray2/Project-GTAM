package com.example.gtam

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.gtam.database.entities.History
import com.example.gtam.ui.theme.GTAMTheme

class Activity6 : ComponentActivity() {
    // Global
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val history: History? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("historyInstance", History::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("historyInstance")
            }
            // UI
            GTAMTheme {
                Column (modifier = Modifier) {
                    HistoryFormat(history)
                }
            }
        }
    }
}

@Composable
private fun HistoryFormat(history: History?) {
    history?.let {
        val status = if (history.status) "Success" else "Failure"
        Text("Type: ${history.type}")
        Text(buildAnnotatedString {
            append("Status: ")
            if (status == "Success")
            withStyle(style = SpanStyle(color = Color.Green)) {
                append(status)
            } else {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(status)
                }
            }
        })
        Text("Reason: ${history.errorMessage}")
        Text("Subject: ${history.subject}")
        Text("Body: ${history.body}")
    }
}