package com.jogging.walking.weightloss.pedometer.steptracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar

object Tools {
    fun getTimeInMillis(date: String): Long {
        val c = Calendar.getInstance()
        c[date.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[2].toInt(), date.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1].toInt() - 1] =
            date.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].toInt()
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 1
        c[Calendar.SECOND] = 1
        c[Calendar.MILLISECOND] = 0
        return c.timeInMillis
    }

    val formattedDateToday: String
        get() {
            val c = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            return sdf.format(c.timeInMillis)
        }

    fun convertTimeToMilliseconds(timeString: String): Long {
        val parts = timeString.split(":")
        if (parts.size != 2) {
            return -1L // or throw an exception
        }
        val minutes = parts[0].toLong()
        val seconds = parts[1].toLong()
        return (minutes * 60 + seconds) * 1000
    }
}