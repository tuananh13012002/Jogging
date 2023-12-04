package com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FloatListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): MutableList<Float> {
        val listType = object : TypeToken<MutableList<Float>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun toJson(list: MutableList<Float>): String {
        return gson.toJson(list)
    }
}