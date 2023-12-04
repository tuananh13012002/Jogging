//package com.example.hahalolofake.data.repository
//
//import androidx.lifecycle.LiveData
//import com.example.hahalolofake.data.db.dao.UserDao
//import com.example.hahalolofake.data.models.User
//import javax.inject.Inject
//
//class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {
//    override suspend fun getUser(): LiveData<MutableList<User>> {
//       return userDao.getUsers()
//    }
//
//    override suspend fun insertUser(user: User) {
//       userDao.insert(user)
//    }
//
//    override suspend fun deleteUser(user: User) {
//        userDao.delete(user)
//    }
//
//    override suspend fun updateUser(user: User) {
//        userDao.update(user)
//    }
//}