package com.benhe.fitlog.logic

import com.benhe.fitlog.data.entity.ExerciseCatalog
import com.benhe.fitlog.data.entity.WorkoutSet
import com.benhe.fitlog.model.BodyRegion
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object LoadCalculator {

    // 1. 明确指定 Map 的类型 [BodyRegion, Float]，解决 Cannot infer type 错误
    // 2. 将 BodyRegion.QUADS 改为 BodyRegion.LEGS (匹配你之前的 8 个部位定义)
    private val REGION_CAPACITY: Map<BodyRegion, Float> = mapOf(
        BodyRegion.CHEST to 5000f,
        BodyRegion.BACK to 6000f,
        BodyRegion.LEGS to 7000f, // 这里之前是 QUADS，导致了 Unresolved reference
        BodyRegion.ARMS to 3000f,
        BodyRegion.SHOULDERS to 3500f,
        BodyRegion.CORE to 4000f,
        BodyRegion.GLUTES to 5000f,
        BodyRegion.CARDIO to 8000f
    ).withDefault { 4000f }

    /**
     * 计算当前时刻 8 个区块的恢复占用率 (0.0 - 1.0+)
     */
    fun calculateRegionLoads(
        sets: List<WorkoutSet>,
        catalog: Map<String, ExerciseCatalog>,
        targetDate: LocalDate = LocalDate.now()
    ): Map<BodyRegion, Float> {
        // 明确指定类型以解决推断错误
        val regionAccumulator = mutableMapOf<BodyRegion, Float>()

        sets.forEach { set ->
            // ✅ 转换日期
            val setDate = java.time.Instant.ofEpochMilli(set.timestamp)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()

            val daysAgo = ChronoUnit.DAYS.between(setDate, targetDate).coerceAtLeast(0)

            // 时间衰减系数
            val decayFactor = (1.0f - daysAgo * 0.15f).coerceAtLeast(0f)
            if (decayFactor <= 0) return@forEach

            // 计算容量逻辑更新：
            // 因为现在是手动备注模式，我们主要通过 set.region 直接获取部位
            // 备注：如果以后你还需要动作库权重，可以保留这部分逻辑
            val volume = (set.weight * set.reps).coerceAtLeast(1f) *
                    (set.rpe?.let { it / 5f } ?: 1.0f) // 这里按 5 星制换算强度

            // 将负荷直接累加到该组所属的部位
            val currentLoad = regionAccumulator[set.region] ?: 0f
            regionAccumulator[set.region] = currentLoad + (volume * decayFactor)
        }

        // 使用 .entries 替换过时的 .values()，解决 IDE 警告
        return BodyRegion.entries.associateWith { region ->
            val totalLoad = regionAccumulator[region] ?: 0f
            // 使用 getValue 获取默认值或指定值
            (totalLoad / (REGION_CAPACITY[region] ?: 4000f)).coerceIn(0f, 1.5f)
        }
    }
}