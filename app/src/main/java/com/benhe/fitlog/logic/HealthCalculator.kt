package com.benhe.fitlog.logic

import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.model.UserProfile

object HealthCalculator {

    /**
     * 1. 计算基础代谢 BMR (基础出厂设置)
     * 使用 Mifflin-St Jeor 公式
     */
    fun calcBMR(
        weight: Double,
        height: Double,
        age: Int,
        gender: String,
        bodyFat: Double? = null
    ): Int {
        // 如果有体脂率，使用 Katch-McArdle 公式（更精准）
        if (bodyFat != null && bodyFat > 0) {
            val leanBodyMass = weight * (1 - bodyFat / 100)
            return (370 + 21.6 * leanBodyMass).toInt()
        }

        // 否则使用 Mifflin-St Jeor 公式
        return if (gender.lowercase() == "male" || gender == "男") {
            (10 * weight + 6.25 * height - 5 * age + 5).toInt()
        } else {
            (10 * weight + 6.25 * height - 5 * age - 161).toInt()
        }
    }

    /**
     * 2. 计算每日总能量消耗 (TDEE)
     * 逻辑：基础代谢 * 生活强度系数
     */
    fun calculateDailyExpenditure(bmr: Int, intensity: LifeIntensity): Int {
        // intensity.factor 在你的定义中是 0.8, 1.0, 1.2, 1.5
        // 注意：通常 TDEE 系数最低是 1.2，如果你想让“久坐”低于 BMR，可以使用 0.8
        return (bmr * intensity.factor).toInt()
    }

    /**
     * 3. 计算综合恢复系数 (用于训练模块)
     * 逻辑：睡眠影响 * 强度消耗影响
     */
    fun calculateRecoveryFactor(sleepHours: Float, intensity: LifeIntensity): Float {
        // 睡眠因子：少于6小时扣分，超过8.5小时加分
        val sleepFactor = when {
            sleepHours < 6f -> 0.7f
            sleepHours > 8.5f -> 1.1f
            else -> 1.0f
        }

        // 强度因子：生活越累，留给肌肉恢复的资源就越少
        // 这里采用 (2.0 - 强度系数)，例如：
        // 强度 0.8 (久坐) -> 恢复系数 1.2 (恢复极快)
        // 强度 1.5 (强力) -> 恢复系数 0.5 (恢复极慢)
        val intensityFactor = (2.0f - intensity.factor).coerceIn(0.5f, 1.5f)

        // 最终系数：用于调节 0.7 - 1.3 的个体化恢复率
        return (sleepFactor * intensityFactor).coerceIn(0.5f, 1.5f)
    }

}