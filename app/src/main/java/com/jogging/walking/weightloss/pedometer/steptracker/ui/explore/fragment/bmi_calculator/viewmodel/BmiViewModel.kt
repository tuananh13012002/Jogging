package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.database.AppDatabase
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User
import com.jogging.walking.weightloss.pedometer.steptracker.data.repository.UserRepository

class BmiViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    val getUsers: LiveData<User>
    var age = MutableLiveData<Int>()
    var gender = MutableLiveData<String>()
    var units = MutableLiveData<String>().apply {
        postValue("km")
    }

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        getUsers = userRepository.getUsers
    }
}