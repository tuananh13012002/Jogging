package com.jogging.walking.weightloss.pedometer.steptracker.utils

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.d("TAGGGG", "Error from coroutine: ${throwable.message}")
}