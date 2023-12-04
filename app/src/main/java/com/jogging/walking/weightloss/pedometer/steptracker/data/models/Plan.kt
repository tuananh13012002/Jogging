package com.jogging.walking.weightloss.pedometer.steptracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="plan_table")
data class Plan(
    val typeGoals: MutableList<String>? = null,
    val goal: Float? = null,
    val duration: Int? = null,
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
