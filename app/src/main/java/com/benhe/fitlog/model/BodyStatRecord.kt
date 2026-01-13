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

// 用于 UI 层传递给凯尔特结的数据模型
data class DailyHealthStatus(
    val calorieIntake: Int,       // 摄入卡路里
    val calorieBurnTarget: Int,   // 目标/消耗卡路里 (TDEE + 运动)
    val workoutFatigue: Int,      // 训练恢复状态值 (0-1000)
    val sleepHours: Float,        // 睡眠时间
    val isHighTrainingLoad: Boolean // 是否大训练量日
)