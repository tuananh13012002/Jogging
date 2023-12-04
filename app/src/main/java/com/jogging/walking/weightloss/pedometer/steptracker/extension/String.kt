package com.jogging.walking.weightloss.pedometer.steptracker.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun String.toDateLong(): Long {
    if(this==""){
        return 0L
    }
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    val cal = Calendar.getInstance()

    if (sdf.parse(this) != null) {
        cal.time = sdf.parse(this)
        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }
    return 0L
}

fun String.toDuration(): String{
    return this.toLong().toDurationFormat()
}

