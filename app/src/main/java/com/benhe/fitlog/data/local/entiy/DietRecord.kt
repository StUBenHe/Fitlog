package com.benhe.fitlog.data.local.entiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diet_records")
data class DietRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodName: String,
    val category: String,
    val quantity: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double, // ✅ 新增碳水字段
    val date: String,
    val timestamp: Long
)