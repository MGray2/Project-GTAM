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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.gtam.ui.theme.Green194


// Class that holds different predefined input gathering functions
class Input(private val styles: Styles) {

    // Text field for gathering input
    @Composable
    fun InputField(text: String, onValueChange: (String) -> Unit, placeholder: String) {
        val config = LocalConfiguration.current

        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = styles.adaptiveMediumFont(config.screenWidthDp)) },
            textStyle = TextStyle(fontSize = styles.adaptiveMediumFont(config.screenWidthDp)),
            modifier = Modifier.padding(9.dp, 0.dp)
                .fillMaxWidth()
                .height(styles.adaptiveSmallHeight(config.screenHeightDp)),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }

    // InputField that hides content like a password field
    @Composable
    fun InputFieldSecure(text: String, onValueChange: (String) -> Unit, placeholder: String, isHidden: Boolean) {
        val config = LocalConfiguration.current

        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = styles.adaptiveMediumFont(config.screenWidthDp)) },
            textStyle = TextStyle(fontSize = styles.adaptiveMediumFont(config.screenWidthDp)),
            modifier = Modifier.padding(9.dp, 0.dp)
                .fillMaxWidth()
                .height(styles.adaptiveSmallHeight(config.screenHeightDp)),
            visualTransformation = if (isHidden) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }

    // Text field with extended height and smaller text
    @Composable
    fun InputFieldLarge(text: String, onValueChange: (String) -> Unit, placeholder: String) {
        val config = LocalConfiguration.current

        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = styles.adaptiveMediumFont(config.screenWidthDp)) },
            textStyle = TextStyle(fontSize = styles.adaptiveMediumFont(config.screenWidthDp)),
            modifier = Modifier.padding(9.dp, 0.dp)
                .fillMaxWidth()
                .height(styles.adaptiveLargeHeight(config.screenHeightDp)),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }

    // Text field with reduced height and even smaller text
    @Composable
    fun InputFieldSmall(text: String, onValueChange: (String) -> Unit, placeholder: String) {
        val config = LocalConfiguration.current

        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(
                placeholder, fontSize = styles.adaptiveSmallFont(config.screenWidthDp),
                modifier = Modifier.fillMaxHeight()
            ) },
            textStyle = TextStyle(fontSize = styles.adaptiveSmallFont(config.screenWidthDp)),
            modifier = Modifier
                .width(styles.adaptiveSmallWidth(config.screenWidthDp))
                .padding(10.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }

    // Text field overload with keyboard parameter
    @Composable
    fun InputField(text: String, onValueChange: (String) -> Unit, placeholder: String, keyboardType: KeyboardType) {
        val config = LocalConfiguration.current

        TextField(
            value = text,
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    onValueChange(newText)
                } },
            placeholder = { Text(placeholder, fontSize = styles.adaptiveMediumFont(config.screenWidthDp)) },
            textStyle = TextStyle(fontSize = styles.adaptiveMediumFont(config.screenWidthDp)),
            modifier = Modifier.padding(9.dp, 0.dp)
                .fillMaxWidth()
                .height(70.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
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
        val config = LocalConfiguration.current

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
                textStyle = TextStyle(fontSize = styles.adaptiveMediumFont(config.screenWidthDp)),
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
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(
                                name,
                                fontSize = styles.adaptiveSmallFont(config.screenWidthDp),
                                color = MaterialTheme.colorScheme.onSurface
                            ) },
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

    // On or Off lightswitch input
    @Composable
    fun InputSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, placeholder: String) {
        val config = LocalConfiguration.current

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.surface),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(placeholder,
                textAlign = TextAlign.Center,
                fontSize = styles.adaptiveSmallFont(config.screenWidthDp),
                modifier = Modifier.padding(10.dp)
            )
            Switch(checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchColors(
                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                    checkedTrackColor = Color.Transparent,
                    checkedBorderColor = MaterialTheme.colorScheme.secondary,
                    checkedIconColor = Color.Transparent,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedTrackColor = Color.Transparent,
                    uncheckedBorderColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedIconColor = Color.Transparent,
                    disabledCheckedThumbColor = Color.Transparent,
                    disabledCheckedTrackColor = Color.Transparent,
                    disabledCheckedBorderColor = Color.Transparent,
                    disabledCheckedIconColor = Color.Transparent,
                    disabledUncheckedThumbColor = Color.Transparent,
                    disabledUncheckedTrackColor = Color.Transparent,
                    disabledUncheckedBorderColor = Color.Transparent,
                    disabledUncheckedIconColor = Color.Transparent
                ),
                modifier = Modifier.padding(10.dp))
        }
    }

}