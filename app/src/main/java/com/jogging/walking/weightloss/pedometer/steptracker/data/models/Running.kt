package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Running(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val duration: String? = null,
    val imageMap: Bitmap? = null,
    val distance: Float? = null,
    val speeds: Float? = null,
    val calo: Float? = null,
    val elevation: Float? = null,
    val paces: MutableList<Float>? = null,
    val detail: Details? = null,
    var timesInMillis: Long?=null,
    var timeStart: String? = null,
    var timeEnd: String? = null
)
