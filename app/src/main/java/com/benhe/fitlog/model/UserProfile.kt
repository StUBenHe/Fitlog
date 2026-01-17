package com.benhe.fitlog.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
data class UserProfile(
    val weight: Double,
    val height: Double,
    val age: Int,
    val gender: String
)
//经期
@Entity(tableName = "period_days")
data class PeriodDay(
    @PrimaryKey val date: LocalDate
)