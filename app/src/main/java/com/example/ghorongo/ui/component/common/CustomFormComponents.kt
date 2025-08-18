package com.example.ghorongo.ui.component.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GenderSelection(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("Male", "Female", "Other", "Prefer not to say")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedGender,
            onValueChange = {},
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select gender")
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender) },
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    }
                )
            }
        }

        // Invisible clickable surface to open dropdown
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable { expanded = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    currentDate: Date?,
    onDateSelected: (Date) -> Unit,
    formatter: SimpleDateFormat
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateString = remember(currentDate) {
        currentDate?.let { formatter.format(it) } ?: ""
    }

    OutlinedTextField(
        value = dateString,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select date")
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0f)
            .clickable { showDatePicker = true }
    )

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        currentDate?.let { calendar.time = it }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDate?.time
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateSelected(Date(millis))
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun BudgetRangeSelector(
    minBudget: Double,
    maxBudget: Double,
    onMinBudgetChanged: (Double) -> Unit,
    onMaxBudgetChanged: (Double) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Budget Range",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = minBudget.toString(),
                onValueChange = {
                    val value = it.toDoubleOrNull() ?: minBudget
                    onMinBudgetChanged(value)
                },
                label = { Text("Min") },
                modifier = Modifier.weight(1f),
                prefix = { Text("$") }
            )

            OutlinedTextField(
                value = maxBudget.toString(),
                onValueChange = {
                    val value = it.toDoubleOrNull() ?: maxBudget
                    onMaxBudgetChanged(value)
                },
                label = { Text("Max") },
                modifier = Modifier.weight(1f),
                prefix = { Text("$") }
            )
        }
    }
}