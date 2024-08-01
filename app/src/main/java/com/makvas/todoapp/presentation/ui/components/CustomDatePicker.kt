package com.makvas.todoapp.presentation.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.makvas.todoapp.R
import java.util.Calendar


@Composable
fun CustomDatePicker(onValueChange: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onValueChange(selectedDate)
        }, year, month, day
    )

    IconButton(
        onClick = { datePickerDialog.show() }
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Date"
        )
    }
}

@Composable
fun CustomTimePicker(
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val selectedTime = "$selectedHour:$selectedMinute"
            onValueChange(selectedTime)
        }, hour, minute, true
    )

    IconButton(
        onClick = { timePickerDialog.show() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.timer),
            contentDescription = "Time"
        )
    }
}