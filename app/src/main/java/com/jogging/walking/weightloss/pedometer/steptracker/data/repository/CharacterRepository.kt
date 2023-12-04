package com.jogging.walking.weightloss.pedometer.steptracker.data.repository

import androidx.paging.PagingData
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.ResultEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacter(page: Int): Flow<PagingData<ResultEntity>>
}