package com.example.gtam

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.gtam.database.entities.Client
import com.example.gtam.database.entities.History
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.components.Styles

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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                    ) {
                        HistoryFormat(history)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryFormat(history: History?) {
    val config = LocalConfiguration.current
    val body = buildAnnotatedString {
        history?.let {
            append("\n")
            append("Name: ${history.clientName}\n")
            append("Address: ${history.clientAddress}\n")
            append("Email: ${history.clientEmail}\n")
            append("Phone: ${history.clientPhone}\n")

            val status = if (history.status) "Success" else "Failure"
            append("Type: ${history.type}\n")

            append("Status: ")
            if (status == "Success")
                withStyle(style = SpanStyle(color = Color.Green)) {
                    append(status)
                } else {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(status)
                }
            }
            append("\n")
            if (!history.status) {
                append("Reason: ${history.errorMessage}\n")
            }
            append("Subject: ${history.subject}\n")
            append("Body: ${history.body}\n")
        }
    }
    Text(body,
        fontSize = Styles().adaptiveSmallFont(config.screenWidthDp),
        lineHeight = 1.5.em
    )
}