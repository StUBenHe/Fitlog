package com.benhe.fitlog.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 生活强度枚举
enum class LifeIntensity(val displayName: String, val factor: Float, val color: Long) {
    SEDENTARY("久坐", 1.2f, 0xFF9E9E9E),      // 灰色
    NORMAL("正常", 1.375f, 0xFF4CAF50),         // 绿色
    LIGHT_EXERCISE("轻微活动", 1.55f, 0xFF2196F3), // 蓝色
    STRENUOUS("强力运动", 1.75f, 0xFFFF9800)     // 橙色
}

@Entity(tableName = "daily_activity")
data class DailyActivity(
    @PrimaryKey val date: String, // 格式：yyyy-MM-dd
    val sleepHours: Float = 8.0f,
    val intensity: LifeIntensity = LifeIntensity.NORMAL,
    val isAfterburnEnabled: Boolean = false // ✅ 添加这个字段，保存后燃开关状态
)