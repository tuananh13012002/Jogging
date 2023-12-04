package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_table")
data class Details(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val title: String? = null,
    val content: String? = null,
    val moods: MutableList<String>? = null,
    val tracks: MutableList<String>? = null,
    val weathers: MutableList<String>? = null,
    val image: String? = null,
)
