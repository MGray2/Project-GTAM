package com.example.gtam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.gtam.database.entities.History
import com.example.gtam.ui.theme.GTAMTheme

class Activity6 : ComponentActivity() {
    // Global
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val history: History? = intent.getParcelableExtra("historyInstance")
            // UI
            GTAMTheme {
                Column (modifier = Modifier) {
                    history?.let {
                        val status = if (history.status) "Success" else "Failure"
                        Text("Type: ${history.type}")
                        Text("Status: $status")
                        Text("Reason: ${history.errorMessage}")
                        Text("Subject: ${history.subject}")
                        Text("Body: ${history.body}")
                    }
                }
            }
        }
    }
}

private fun historyFormat(history: History) {

}