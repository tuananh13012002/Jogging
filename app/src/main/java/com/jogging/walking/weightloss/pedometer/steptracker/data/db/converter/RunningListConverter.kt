package com.jogging.walking.weightloss.pedometer.steptracker.data.db.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import java.io.ByteArrayOutputStream

class RunningListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): MutableList<Running> {
        val listType = object : TypeToken<MutableList<Running>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun toJson(list: MutableList<Running>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }

    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}