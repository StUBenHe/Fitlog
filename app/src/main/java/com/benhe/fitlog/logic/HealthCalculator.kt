package com.benhe.fitlog.logic

import com.benhe.fitlog.model.LifeIntensity

object HealthCalculator {

    // ✅ 定义后燃效应系数：1.1 表示提升 10% 的代谢
    private const val AFTERBURN_FACTOR = 1.1f

    /**
     * 1. 计算基础代谢 BMR
     */
    fun calcBMR(
        weight: Double,
        height: Double,
        age: Int,
        gender: String,
        bodyFat: Double? = null
    ): Int {
        if (bodyFat != null && bodyFat > 0) {
            val leanBodyMass = weight * (1 - bodyFat / 100)
            return (370 + 21.6 * leanBodyMass).toInt()
        }

        return if (gender.lowercase() == "male" || gender == "男") {
            (10 * weight + 6.25 * height - 5 * age + 5).toInt()
        } else {
            (10 * weight + 6.25 * height - 5 * age - 161).toInt()
        }
    }

    /**
     * 2. 计算每日总能量消耗 (TDEE)
     * 逻辑：(基础代谢 * 生活强度系数) * [后燃效应开关]
     */
    fun calculateDailyExpenditure(
        bmr: Int,
        intensity: LifeIntensity,
        isAfterburnEnabled: Boolean = false // ✅ 新增开关参数
    ): Int {
        // 基础消耗 = BMR * 生活强度 (如 0.8, 1.0, 1.2, 1.5)
        val baseTDEE = bmr * intensity.factor

        // 如果开启后燃效应，在基础消耗上再乘 1.1
        val finalExpenditure = if (isAfterburnEnabled) {
            baseTDEE * AFTERBURN_FACTOR
        } else {
            baseTDEE
        }

        return finalExpenditure.toInt()
    }

    /**
     * 3. 计算综合恢复系数 (用于训练模块)
     */
    fun calculateRecoveryFactor(sleepHours: Float, intensity: LifeIntensity): Float {
        val sleepFactor = when {
            sleepHours < 6f -> 0.7f
            sleepHours > 8.5f -> 1.1f
            else -> 1.0f
        }

        val intensityFactor = (2.0f - intensity.factor).coerceIn(0.5f, 1.5f)

        return (sleepFactor * intensityFactor).coerceIn(0.5f, 1.5f)
    }
}