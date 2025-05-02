package com.example.gtam

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Date {
    fun getTodayFullDate(): String {
        val calendar = Calendar.getInstance()
        val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
        val date = SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(calendar.time)
        val time = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(calendar.time)
        return "$day, $date $time"
    }
}