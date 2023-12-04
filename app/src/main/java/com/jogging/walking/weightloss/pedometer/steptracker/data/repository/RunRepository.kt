package com.jogging.walking.weightloss.pedometer.steptracker.data.repository

import androidx.lifecycle.LiveData
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.dao.RunDao
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running

class RunRepository(private val runDao: RunDao) {
    val getRuns: LiveData<MutableList<Running>> = runDao.getRuns()

    suspend fun getRunsNormal(): MutableList<Running> {
        return runDao.getRunsNormal()
    }

    fun getRunById(id: Long): LiveData<Running> {
        return runDao.getRunById(id)
    }

    suspend fun insertRun(running: Running) {
        runDao.insert(running)
    }

    suspend fun deleteRun(running: Running) {
        runDao.delete(running)
    }

    suspend fun updateRun(running: Running) {
        runDao.update(running)
    }

    suspend fun deleteAll(){
        runDao.deleteAll()
    }
}