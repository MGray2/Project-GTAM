package com.example.gtam.ui.theme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gtam.R
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData


class Components {

    @Composable
    fun MainMenuButton(context: Context, activityClass: Class<out Activity>, text: String) {
        Button(
            onClick = {
                val intent = Intent(context, activityClass)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Green194,
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
                .height(100.dp)
                .padding(10.dp, 0.dp, 10.dp, 20.dp)

        ) {
            Text(text, fontSize = 30.sp)
        }
    }

    @Composable
    fun ButtonGeneric(onClick: () -> Unit, placeholder: String) {
        Button(onClick = { onClick() },
            modifier = Modifier.padding(10.dp)
                .fillMaxWidth()
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
                Text(placeholder, fontSize = 24.sp)
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
                contentColor = Color.DarkGray
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
    fun InputField(text: String, onValueChange: (String) -> Unit, placeholder: String) {

        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 26.sp) },
            textStyle = TextStyle(fontSize = 26.sp),
            modifier = Modifier.padding(9.dp, 0.dp)
                .fillMaxWidth()
                .height(70.dp)
        )
    }

    @Composable
    fun InputFieldLarge(text: String, onValueChange: (String) -> Unit, placeholder: String) {

        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 24.sp) },
            textStyle = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(9.dp, 0.dp)
                .fillMaxWidth()
                .height(120.dp)
        )
    }

    @Composable
    fun InputFieldNumber(text: String, onValueChange: (String) -> Unit, placeholder: String, keyboardType: KeyboardType) {

        TextField(
            value = text,
            onValueChange = { newText -> if (newText.all { it.isDigit() }) {
                onValueChange(newText)
            } },
            placeholder = { Text(placeholder, fontSize = 26.sp) },
            textStyle = TextStyle(fontSize = 26.sp),
            modifier = Modifier.padding(9.dp, 0.dp)
                .fillMaxWidth()
                .height(70.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
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

    @Composable
    fun LittleText(text: String, modifier: Modifier) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
                    .padding(0.dp, 15.dp, 0.dp, 0.dp)
            )
        }
    }

    @Composable
    fun LittleTextWIButton(text: String, message: String) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(text, fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentWidth()
                    .padding(0.dp, 15.dp, 0.dp, 0.dp)
            )
            InfoButton(message)
        }
    }

    // Composable dropdown function
    @Composable
    fun InputDropDown(
        options: List<Pair<Long, String>>,
        selectedId: MutableState<Long?>,
        placeholder: String
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedName by remember { mutableStateOf(placeholder) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedName,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth() // Ensures menu width matches text field
            ) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedId.value = id  // Store the selected ID
                            selectedName = name    // Display the selected name
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun InputDropDownLive(
        optionsLiveData: LiveData<List<Pair<Long, String>>>, // LiveData instead of List
        selectedId: MutableState<Long?>,
        placeholder: String
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedName by remember { mutableStateOf(placeholder) }

        // Observe LiveData to get the latest options
        val options by optionsLiveData.observeAsState(initial = emptyList())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedName,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedId.value = id  // Store the selected ID
                            selectedName = name    // Display the selected name
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    // Composable dropdown function
    @Composable
    fun InputDropDownNullable(
        options: List<Pair<Long, String?>>,
        selectedId: MutableState<Long?>,
        placeholder: String
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedName by remember { mutableStateOf(placeholder) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedName,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth() // Ensures menu width matches text field
            ) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name!!) },
                        onClick = {
                            selectedId.value = id  // Store the selected ID
                            selectedName = name!!    // Display the selected name
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    fun ShowToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}