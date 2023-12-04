package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val username: String? = null,
    val avatar: String? = null,
    val age: Int? = null,
    val height: String? = null,
    val weight: String? = null,
    var listRun:MutableList<Running>?=null,
    val gender: String? = null,
    var units:String? = null,
)