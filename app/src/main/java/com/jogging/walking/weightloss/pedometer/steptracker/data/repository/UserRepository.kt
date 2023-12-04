package com.jogging.walking.weightloss.pedometer.steptracker.data.repository

import androidx.lifecycle.LiveData
import com.jogging.walking.weightloss.pedometer.steptracker.data.db.dao.UserDao
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User

class UserRepository(private val userDao: UserDao) {
    val getUsers: LiveData<User> = userDao.getUsers()

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }
}