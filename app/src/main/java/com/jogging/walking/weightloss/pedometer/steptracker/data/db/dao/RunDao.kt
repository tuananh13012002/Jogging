package com.jogging.walking.weightloss.pedometer.steptracker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running

@Dao
interface RunDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(running: Running?)

    @Query("SELECT * FROM running_table ORDER BY id DESC")
    fun getRuns(): LiveData<MutableList<Running>>

    @Query("SELECT * FROM running_table ORDER BY id DESC")
    suspend fun getRunsNormal(): MutableList<Running>

    @Query("SELECT * FROM running_table WHERE id = :id")
    fun getRunById(id: Long): LiveData<Running>

    @Delete
    suspend fun delete(running: Running?)

    @Update
    suspend fun update(running: Running?)

    @Query("DELETE FROM running_table")
    suspend fun deleteAll()
}