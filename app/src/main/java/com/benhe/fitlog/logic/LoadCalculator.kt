package com.benhe.fitlog.logic

import com.benhe.fitlog.data.entity.ExerciseCatalog
import com.benhe.fitlog.data.entity.WorkoutSet
import com.benhe.fitlog.model.BodyRegion
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object LoadCalculator {

    // 定义每个区块的“额定最大周负荷”（作为 100% 参照物）
    // 这里的数值可以根据用户水平调整，目前设为基础值
    private val REGION_CAPACITY = mapOf(
        BodyRegion.CHEST to 5000f,
        BodyRegion.BACK to 6000f,
        BodyRegion.QUADS to 7000f,
        // ... 其他区块
    ).withDefault { 4000f }

    /**
     * 计算当前时刻 8 个区块的恢复占用率 (0.0 - 1.0+)
     */
    fun calculateRegionLoads(
        sets: List<WorkoutSet>,
        catalog: Map<String, ExerciseCatalog>,
        targetDate: LocalDate = LocalDate.now()
    ): Map<BodyRegion, Float> {
        val regionAccumulator = mutableMapOf<BodyRegion, Float>().withDefault { 0f }

        sets.forEach { set ->
            val exercise = catalog[set.exerciseId] ?: return@forEach

            // ✅ 将毫秒时间戳转换为 LocalDate
            val setDate = java.time.Instant.ofEpochMilli(set.timestamp)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()

            val daysAgo = ChronoUnit.DAYS.between(
                setDate, // ✅ 使用转换后的日期
                targetDate
            ).coerceAtLeast(0)

            // 1. 时间衰减系数：0天(100%), 1天(85%), 2天(70%)... 7天后归零
            val decayFactor = (1.0f - daysAgo * 0.15f).coerceAtLeast(0f)
            if (decayFactor <= 0) return@forEach

            // 2. 计算该组的总负荷
            val volume = set.weight * set.reps * (set.rpe?.let { it / 10f } ?: 1.0f)

            // 3. 将负荷按权重分配到各个区块
            exercise.regionWeights.forEach { (region, weight) ->
                val contribution = volume * weight * decayFactor
                regionAccumulator[region] = (regionAccumulator[region] ?: 0f) + contribution
            }
        }

        // 4. 转化为百分比 (Load / Capacity)
        return BodyRegion.values().associateWith { region ->
            val totalLoad = regionAccumulator[region] ?: 0f
            (totalLoad / REGION_CAPACITY.getValue(region)).coerceIn(0f, 1.5f)
        }
    }
}