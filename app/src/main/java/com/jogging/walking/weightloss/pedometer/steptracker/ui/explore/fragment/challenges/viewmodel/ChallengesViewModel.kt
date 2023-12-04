package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.challenges.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.database.AppDatabase
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.data.repository.RunRepository

class ChallengesViewModel(application: Application) : AndroidViewModel(application)  {
    private val runRepository: RunRepository
    val getRuns: LiveData<MutableList<Running>>

    init {
        val runDao = AppDatabase.getDatabase(application).runDao()
        runRepository = RunRepository(runDao)
        getRuns = runRepository.getRuns
    }
}