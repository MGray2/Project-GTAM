package com.example.gtam.ui.theme.components

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gtam.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

// Class that holds different predefined button functions
class Buttons(private val styles: Styles) {
    @Composable
    fun MainMenuButton(context: Context, activityClass: Class<out Activity>, text: String) {
        val config = LocalConfiguration.current

        Button(
            onClick = {
                val intent = Intent(context, activityClass)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
                .height(100.dp)
                .padding(10.dp, 0.dp, 10.dp, 20.dp)

        ) {
            Text(text, fontSize = styles.adaptiveLargeFont(config.screenWidthDp))
        }
    }

    @Composable
    fun ButtonGeneric(onClick: () -> Unit, placeholder: String) {
        val config = LocalConfiguration.current

        Button(onClick = { onClick() },
            modifier = Modifier.padding(10.dp)
                .fillMaxWidth()
                .height(styles.adaptiveSmallHeight(config.screenHeightDp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(placeholder, fontSize = styles.adaptiveMediumFont(config.screenWidthDp))
        }
    }

    @Composable
    fun ButtonGeneric(onClick: () -> Unit, placeholder: String, modifier: Modifier) {
        val config = LocalConfiguration.current

        Button(onClick = { onClick() },
            modifier = modifier.padding(10.dp)
                .height(styles.adaptiveSmallHeight(config.screenHeightDp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(placeholder, fontSize = styles.adaptiveMediumFont(config.screenWidthDp))
        }
    }

    @Composable
    fun ButtonDoubleClick(onClick: () -> Unit, placeholder: String, successMessage: String, failMessage: String, context: Context) {
        var lastClickTime by remember { mutableLongStateOf(0L) }
        val config = LocalConfiguration.current

        Button(onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < 1000) {
                onClick()
                showToast(successMessage, context) // double-clicked in time
            } else {
                showToast(failMessage, context) // too slow
            }
            lastClickTime = currentTime },
            modifier = Modifier.padding(10.dp)
                .fillMaxWidth()
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(placeholder, fontSize = styles.adaptiveMediumFont(config.screenWidthDp))
        }
    }

    @Composable
    fun DeleteButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    fun RemoveButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Remove",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    fun TodayButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Today's Date",
                modifier = Modifier.size(24.dp)
            )
        }
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
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(imageVector = Icons.Filled.Info,
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
    private fun CustomMessageBox(showDialog: Boolean, onDismiss: () -> Unit, message: String) {
        if (showDialog) {
            Dialog(onDismissRequest = { onDismiss() }) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                        .padding(20.dp),
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

    @Composable
    fun DatePickerButton(
        onDateSelected: (String) -> Unit
    ) {
        val context = LocalContext.current
        val calendar = remember { Calendar.getInstance() }
        val themedContext = ContextThemeWrapper(context, R.style.DatePickerThemeCustom)

        val datePickerDialog = remember {
            DatePickerDialog(
                themedContext,
                { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(year, month, dayOfMonth)

                    val sdf = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
                    val formattedDate = sdf.format(cal.time)
                    onDateSelected(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        TodayButton { datePickerDialog.show() }
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun HistoryButton(onClick: () -> Unit, placeholder: String, status: Boolean) {
        val config = LocalConfiguration.current
        val colors = listOf(
            MaterialTheme.colorScheme.surface,
            if (status) MaterialTheme.colorScheme.surfaceBright
            else MaterialTheme.colorScheme.surfaceDim)

        Button(onClick = { onClick() },
            modifier = Modifier.padding(2.dp, 0.dp)
                .fillMaxWidth()
                .height(styles.adaptiveSmallHeight(config.screenHeightDp))
                .background(
                    Brush.linearGradient(
                        colors,
                        start = Offset(500.0F, 0.0F),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(placeholder, fontSize = 24.sp, lineHeight = 30.sp, softWrap = false)
        }
    }
}