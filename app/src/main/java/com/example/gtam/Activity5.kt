package com.example.gtam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gtam.database.entities.History
import com.example.gtam.database.factory.HistoryFactory
import com.example.gtam.database.viewmodel.HistoryViewModel
import com.example.gtam.ui.theme.GTAMTheme
import com.example.gtam.ui.theme.components.Banners
import com.example.gtam.ui.theme.components.Buttons
import com.example.gtam.ui.theme.components.Input

class Activity5 : ComponentActivity() {
    // Global
    private val banner = Banners()
    private val button = Buttons()
    private val input = Input()
    private val historyVM: HistoryViewModel by viewModels { HistoryFactory(MyApp.historyRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Local
            val context = LocalContext.current
            val historyList by historyVM.allHistory.observeAsState(initial = emptyList())

            // UI
            GTAMTheme {
                Column (modifier = Modifier) {
                    banner.CustomHeader("Message History")
                    var itemCount = 0
                    historyList.forEach { history ->
                        HistoryWindow(itemCount, history, button, context)
                        itemCount++
                    }
                }
            }
        }
    }
}

// temp
private fun doNothing() {
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
        HistoryRow(modifier = Modifier.background(Color.LightGray), iterable, button, context)
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
        button.ButtonGeneric({ historyButton(context, iterable) }, iterable.date)
    }
}