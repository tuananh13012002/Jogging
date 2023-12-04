package com.jogging.walking.weightloss.pedometer.steptracker.extension

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toDurationFormat(): String{
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}