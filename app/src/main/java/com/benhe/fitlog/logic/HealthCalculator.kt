package com.benhe.fitlog.logic

object HealthCalculator {
    /**
     * 计算基础代谢 BMR
     */
    fun calcBMR(
        weight: Double,
        height: Double,
        age: Int,
        gender: String,
        bodyFat: Double? = null
    ): Int {
        // 如果有体脂率，使用 Katch-McArdle 公式
        if (bodyFat != null && bodyFat > 0) {
            val leanBodyMass = weight * (1 - bodyFat / 100)
            return (370 + 21.6 * leanBodyMass).toInt()
        }

        // 否则使用 Mifflin-St Jeor 公式
        return if (gender == "male") {
            (10 * weight + 6.25 * height - 5 * age + 5).toInt()
        } else {
            (10 * weight + 6.25 * height - 5 * age - 161).toInt()
        }
    }
}