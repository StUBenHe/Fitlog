package com.benhe.fitlog.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diet_records")
data class DietRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodName: String,
    val category: String, // "碳水", "蛋白质", "维生素"
    val quantity: String, // 例如 "100g"
    val calories: Double,
    val protein: Double,
    val date: String,     // 格式：yyyy-MM-dd
    val timestamp: Long
)