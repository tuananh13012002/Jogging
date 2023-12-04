package com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Details

class DetailsConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): Details? {
        return gson.fromJson(json, Details::class.java)
    }

    @TypeConverter
    fun toJson(details: Details?): String {
        return gson.toJson(details)
    }
}