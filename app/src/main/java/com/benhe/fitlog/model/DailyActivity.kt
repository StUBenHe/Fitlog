package com.benhe.fitlog.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 生活强度枚举
enum class LifeIntensity(
    val displayName: String,
    val factor: Float,       // TDEE 能量系数 (如 1.2, 1.375)
    val color: Long,
    val proteinMultiplier: Float, // ✅ 新增：蛋白质系数 (g/kg)
    val recoveryImpact: Float    // ✅ 新增：对恢复速度的影响
) {
    SEDENTARY("久坐", 1.1f, 0xFF9E9E9E, 0.9f, 1.3f),
    NORMAL("正常", 1.35f, 0xFF4CAF50, 1.3f, 1.1f),
    LIGHT_EXERCISE("轻微活动", 1.5f, 0xFF2196F3, 1.8f, 1.0f),
    STRENUOUS("强力运动", 1.75f, 0xFFFF9800, 2.2f, 0.9f)
}
@Entity(tableName = "daily_activity")
data class DailyActivity(
    @PrimaryKey val date: String, // 格式：yyyy-MM-dd
    val sleepHours: Float = 8.0f,
    val intensity: LifeIntensity = LifeIntensity.NORMAL,
    val isAfterburnEnabled: Boolean = false // ✅ 添加这个字段，保存后燃开关状态
)

