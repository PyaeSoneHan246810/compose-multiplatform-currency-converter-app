package com.example.currencyapp.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getFormattedCurrentDateTime(): String {
    val currentTimeStamp = Clock.System.now()
    val currentDate = currentTimeStamp.toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDayOfMonth = currentDate.dayOfMonth.toString()
    val currentMonth = currentDate.month.toString().lowercase().replaceFirstChar {  firstChar ->
        firstChar.uppercase()
    }
    val currentYear = currentDate.year.toString()
    return "$currentDayOfMonth $currentMonth, $currentYear"
}