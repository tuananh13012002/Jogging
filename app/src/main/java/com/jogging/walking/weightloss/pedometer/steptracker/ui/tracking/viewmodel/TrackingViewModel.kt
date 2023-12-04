package com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.database.AppDatabase
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User
import com.jogging.walking.weightloss.pedometer.steptracker.data.repository.RunRepository
import com.jogging.walking.weightloss.pedometer.steptracker.data.repository.UserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackingViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    private val runRepository: RunRepository
    val getUsers: LiveData<User>
    val getRuns: LiveData<MutableList<Running>>
    var distance = MutableLiveData<Float>().apply {
        postValue(0f)
    }

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        val runDao = AppDatabase.getDatabase(application).runDao()
        userRepository = UserRepository(userDao)
        runRepository = RunRepository(runDao)
        getUsers = userRepository.getUsers
        getRuns = runRepository.getRuns
    }

    suspend fun getRunsNormal(): MutableList<Running> {
        return runRepository.getRunsNormal()
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.d("TAGggg", "update: ${throwable .message}")
        }) {
            userRepository.updateUser(user)
        }
    }

    fun insertRun(running: Running) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.d("TAGggg", "update: ${throwable.message}")
        }) {
            runRepository.insertRun(running)
        }
    }
}