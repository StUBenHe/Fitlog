package com.benhe.fitlog.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_stat_history")
data class BodyStatRecord(
    // 使用 Long 类型的时间戳 (毫秒)，方便排序和整点计算
    @PrimaryKey val timestamp: Long,
    val weight: Float,
    val bodyFatRate: Float,
    val dateString: String // yyyy-MM-dd 用于显示
)

