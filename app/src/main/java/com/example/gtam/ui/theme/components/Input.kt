package com.example.gtam.ui.theme.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.gtam.ui.theme.Green194
import com.example.gtam.ui.theme.Green240

// Class that holds different predefined input gathering functions
class Input {

    // Text field for gathering input
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

    // Text field with extended height and smaller text
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

    // Text field with reduced height and even smaller text
    @Composable
    fun InputFieldSmall(text: String, onValueChange: (String) -> Unit, placeholder: String) {

        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(
                placeholder, fontSize = 20.sp,
                modifier = Modifier.fillMaxHeight()
            ) },
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .width(140.dp)
                .padding(10.dp)
        )
    }

    // Text field with number keyboard instead of full keyboard
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

    // Input dropdown that uses LiveData
    @Composable
    fun InputDropDown(
        optionsLiveData: LiveData<List<Pair<Long, String>>>,
        selectedId: MutableState<Long?>,
        placeholder: String,
        onReset: (() -> Unit) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedName by remember { mutableStateOf(placeholder) }

        // Observe LiveData to get the latest options
        val options by optionsLiveData.observeAsState(initial = emptyList())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
                .padding(10.dp, 0.dp)
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
                        text = { Text(name, fontSize = 20.sp) },
                        onClick = {
                            selectedId.value = id  // Store the selected ID
                            selectedName = name    // Display the selected name
                            expanded = false
                        }
                    )
                }
            }
        }
        fun resetDropdown() {
            selectedId.value = null
            selectedName = placeholder
        }

        onReset { resetDropdown() }
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

    @Composable
    fun InputSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, placeholder: String) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.LightGray),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(placeholder, textAlign = TextAlign.Center, fontSize = 20.sp)
            Switch(checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchColors(
                    checkedThumbColor = Green194,
                    checkedTrackColor = Color.Transparent,
                    checkedBorderColor = Green194,
                    checkedIconColor = Color.Transparent,
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedBorderColor = Color.DarkGray,
                    uncheckedIconColor = Color.Transparent,
                    disabledCheckedThumbColor = Color.Transparent,
                    disabledCheckedTrackColor = Color.Transparent,
                    disabledCheckedBorderColor = Color.Transparent,
                    disabledCheckedIconColor = Color.Transparent,
                    disabledUncheckedThumbColor = Color.Transparent,
                    disabledUncheckedTrackColor = Color.Transparent,
                    disabledUncheckedBorderColor = Color.Transparent,
                    disabledUncheckedIconColor = Color.Transparent
                ))
        }
    }

}