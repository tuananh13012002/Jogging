package com.jogging.walking.weightloss.pedometer.steptracker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User?)

    @Query("SELECT * FROM user_table")
    fun getUsers(): LiveData<User>

    @Delete
    suspend fun delete(user: User?)

    @Update
    suspend fun update(user: User?)

}