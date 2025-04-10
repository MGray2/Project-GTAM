package com.example.gtam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gtam.database.entities.History
import com.example.gtam.database.factory.HistoryFactory
import com.example.gtam.database.viewmodel.HistoryViewModel
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.components.Banners
import com.example.gtam.ui.theme.components.Buttons
import com.example.gtam.ui.theme.components.Styles

class Activity5 : ComponentActivity() {
    // Global
    private val banner = Banners(Styles())
    private val button = Buttons(Styles())
    private val historyVM: HistoryViewModel by viewModels { HistoryFactory(MyApp.historyRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val context = LocalContext.current
            val historyList by historyVM.allHistory.observeAsState(initial = emptyList())
            val messageSuccess = "History Cleared"
            val messageFail = "Double-Click to clear history."

            // UI
            GTAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier.fillMaxSize()) {
                        banner.CustomHeader("Message History")
                        Column (modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(1f)
                        ) {
                            var itemCount = 0
                            historyList.forEach { history ->
                                HistoryWindow(itemCount, history, button, context)
                                itemCount++
                            }
                        }
                        button.ButtonDoubleClick({ historyVM.deleteAllHistory() }, "Clear History", messageSuccess, messageFail, context)
                    }
                }
            }
        }
    }
}

// Go to activity 6
private fun historyButton(context: Context, history: History) {
    val intent = Intent(context, Activity6::class.java).apply {
        putExtra("historyInstance", history)
    }
    context.startActivity(intent)
}

@Composable
private fun HistoryWindow(counter: Int, iterable: History, button: Buttons, context: Context) {
    if (counter % 2 != 0) {
        HistoryRow(modifier = Modifier, iterable, button, context)
    } else {
        HistoryRow(modifier = Modifier, iterable, button, context)
    }
}

@Composable
private fun HistoryRow(modifier: Modifier, iterable: History, button: Buttons, context: Context) {
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        button.HistoryButton({ historyButton(context, iterable) }, iterable.date)
    }
}