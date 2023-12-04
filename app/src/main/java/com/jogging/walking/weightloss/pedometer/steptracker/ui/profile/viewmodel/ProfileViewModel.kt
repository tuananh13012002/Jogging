package com.jogging.walking.weightloss.pedometer.steptracker.ui.profile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.database.AppDatabase
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User
import com.jogging.walking.weightloss.pedometer.steptracker.data.repository.UserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    val getUsers: LiveData<User>
    var urlAvt = MutableLiveData<String>()
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

    fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.d("TAGggg", "update: ${throwable.message}")
        }) {
            userRepository.updateUser(user)
        }
    }

}