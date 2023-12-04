package com.jogging.walking.weightloss.pedometer.steptracker.data.api.service

import com.jogging.walking.weightloss.pedometer.steptracker.data.models.ResponseApi
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("character")
    suspend fun getCharacter(@Query("page") page: Int) : ResponseApi
}