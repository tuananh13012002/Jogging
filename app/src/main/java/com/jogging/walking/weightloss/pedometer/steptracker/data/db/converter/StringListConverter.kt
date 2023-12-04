package com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): MutableList<String> {
        val listType = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun toJson(list: MutableList<String>): String {
        return gson.toJson(list)
    }
}