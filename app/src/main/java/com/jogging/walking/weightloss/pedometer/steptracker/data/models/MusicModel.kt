package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MusicModel (
    @SerializedName("PopMusic")
    val popMusic: ArrayList<Music>,
    @SerializedName("HiphopMusic")
    val hiphopMusic: ArrayList<Music>,
    @SerializedName("RapMusic")
    val rockMusic: ArrayList<Music>,
){
    @Parcelize
    data class Music(
        @SerializedName("Id")
        val id: Int,
        @SerializedName("Name")
        val name: String,
        @SerializedName("href")
        val url: String,
        val duration: String
    ): Parcelable
}