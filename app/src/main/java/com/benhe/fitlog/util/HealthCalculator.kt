package com.benhe.fitlog.util

import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.model.UserProfile

object HealthCalculator {

    // 后燃效应系数：提升 10% 的代谢
    private const val AFTERBURN_FACTOR = 1.1f

    /**
     * 1. 计算基础代谢 BMR
     */
    fun calcBMR(user: UserProfile): Int {
        val weight = user.weight
        val height = user.height
        val age = user.age
        val gender = user.gender

        return if (gender.lowercase() == "male" || gender == "男") {
            (10 * weight + 6.25 * height - 5 * age + 5).toInt()
        } else {
            (10 * weight + 6.25 * height - 5 * age - 161).toInt()
        }
    }

    /**
     * 2. 计算每日总能量消耗 (TDEE)
     */
    fun calculateDailyExpenditure(
        bmr: Int,
        intensity: LifeIntensity,
        isAfterburnEnabled: Boolean = false
    ): Int {
        // 使用枚举中定义的 factor (1.1 - 1.75)
        val baseTDEE = bmr * intensity.factor

        val finalExpenditure = if (isAfterburnEnabled) {
            baseTDEE * AFTERBURN_FACTOR
        } else {
            baseTDEE
        }

        return finalExpenditure.toInt()
    }


    /**
     * 4. 计算蛋白质目标 (克)
     */
    fun calculateProteinTarget(user: UserProfile, intensity: LifeIntensity): Float {
        val genderBonus = if (user.gender.lowercase() == "male" || user.gender == "男") 1.0f else 0.9f
        return (user.weight * intensity.proteinMultiplier * genderBonus).toFloat()
    }
}