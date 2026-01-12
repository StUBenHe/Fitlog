package com.benhe.fitlog.logic

import com.benhe.fitlog.data.entity.WorkoutSet
import com.benhe.fitlog.model.BodyRegion
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object LoadCalculator {

    // 基础恢复速率：每天恢复 30% (即 0.3f)
    private const val DAILY_RECOVERY_RATE = 0.3f

    /**
     * 计算各部位当前状态 (1.0 = 满血, 0.0 = 彻底力竭)
     */
    fun calculateRegionStatus(
        sets: List<WorkoutSet>,
        sleepHours: Float = 8f,
        proteinGrams: Float = 80f,
        targetDate: LocalDate = LocalDate.now()
    ): Map<BodyRegion, Float> {
        // 初始状态全满
        val statusMap = BodyRegion.entries.associateWith { 1.0f }.toMutableMap()

        // 按部位分组计算消耗
        sets.groupBy { it.region }.forEach { (region, regionSets) ->
            var currentStatus = 1.0f

            // 排序，确保按时间顺序计算扣除
            val sortedSets = regionSets.sortedBy { it.timestamp }

            sortedSets.forEach { set ->
                val setDate = java.time.Instant.ofEpochMilli(set.timestamp)
                    .atZone(ZoneId.systemDefault()).toLocalDate()

                // 计算这组动作距离目标日期过了几天
                val daysAgo = ChronoUnit.DAYS.between(setDate, targetDate).coerceAtLeast(0)

                // 1. 扣除消耗：5星扣 0.4, 1星扣 0.08 (可根据需求调整)
                val drain = (set.rpe ?: 1) * 0.08f

                // 2. 计算恢复：考虑到睡眠和饮食因子
                val sleepFactor = (sleepHours / 8f).coerceIn(0.5f, 1.5f)
                val dietFactor = if (proteinGrams > 60f) 1.2f else 0.8f
                val recovery = daysAgo * DAILY_RECOVERY_RATE * sleepFactor * dietFactor

                // 最终状态 = 初始(1.0) - 消耗 + 恢复
                currentStatus = (currentStatus - drain + recovery).coerceIn(0f, 1f)
            }
            statusMap[region] = currentStatus
        }
        return statusMap
    }
}