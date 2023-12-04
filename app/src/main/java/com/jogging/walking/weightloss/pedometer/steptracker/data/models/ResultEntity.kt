package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import com.google.gson.annotations.SerializedName

class ResultEntity(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image")
    var image: String? = null
)
