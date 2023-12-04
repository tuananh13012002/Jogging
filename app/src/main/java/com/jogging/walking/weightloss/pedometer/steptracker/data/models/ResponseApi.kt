package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import com.google.gson.annotations.SerializedName

class ResponseApi(
    @SerializedName("results")
    var results: MutableList<ResultEntity>? = null
)