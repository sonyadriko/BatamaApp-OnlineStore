package com.example.tokoonline.util

import com.example.tokoonline.constanst.Constant
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}

fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    df.timeZone = TimeZone.getTimeZone("UTC")
    return df.parse(date)?.time ?: 0
}

fun convertStringToCalendar(date: String): Calendar {
    val fromDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    fromDate.timeZone = TimeZone.getTimeZone("UTC")
    val date = fromDate.parse(date)
    val cal = Calendar.getInstance(Locale.getDefault())
    cal.time = date
    return cal
}

fun String.toRole(): Constant.Role? {
    return Constant.Role.values().find {
        it.name.lowercase() == this
    }
}