package com.benhe.fitlog.logic

import com.benhe.fitlog.data.entity.WorkoutSet
import com.benhe.fitlog.model.BodyRegion
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.model.UserProfile
import kotlin.math.pow

object LoadCalculator {

    private const val RECOVERY_HOURS_LARGE = 72.0f
    private const val RECOVERY_HOURS_NORMAL = 48.0f
    private const val RECOVERY_EXPONENT = 1.75 // 曲率系数 k

    /**
     * 核心方法：计算当前身体状态
     */
    fun calculateRegionStatus(
        sets: List<WorkoutSet>,
        userProfile: UserProfile,
        sleepHours: Float,
        intensity: LifeIntensity,
        isAfterburnActive: Boolean,
        proteinGrams: Float,
        targetTimestamp: Long = System.currentTimeMillis()
    ): Map<BodyRegion, Float> {

        // 1. 获取动态蛋白质目标
        val proteinThreshold = HealthCalculator.calculateProteinTarget(userProfile, intensity)

        // 2. 计算综合恢复修正系数 (Modifier)
        val recoveryModifier = calculateModifier(
            sleepHours = sleepHours,
            proteinGrams = proteinGrams,
            proteinThreshold = proteinThreshold,
            intensity = intensity,
            afterburn = isAfterburnActive
        )

        // 3. 处理部位状态计算
        return processStatusLogic(sets, recoveryModifier, targetTimestamp)
    }

    /**
     * 综合恢复效率计算
     * 考虑：睡眠(50%) + 营养(30%) + 生活强度恢复影响(10%) + 后燃效应(10%)
     */
    private fun calculateModifier(
        sleepHours: Float,
        proteinGrams: Float,
        proteinThreshold: Float,
        intensity: LifeIntensity,
        afterburn: Boolean
    ): Float {
        // A. 睡眠得分 (基准 8h)
        val sleepScore = (sleepHours / 8f).coerceIn(0.5f, 1.2f) * 0.5f

        // B. 营养得分 (对比动态阈值，最高 1.5 倍蛋白摄入有效)
        val proteinScore = if (proteinThreshold > 0) {
            (proteinGrams / proteinThreshold).coerceIn(0.5f, 1.5f) * 0.3f
        } else 0.3f

        // C. 生活强度恢复影响 (久坐环境更利于修复: 1.3)
        val intensityScore = intensity.recoveryImpact * 0.1f

        // D. 后燃效应 (开启后，代谢提升，血液循环加快，额外增加 10% 恢复效率系数)
        val afterburnScore = if (afterburn) 0.1f else 0.0f

        // 最终修正值 (1.0 为完美状态下的标准速度)
        return (sleepScore + proteinScore + intensityScore + afterburnScore)
    }

    private fun processStatusLogic(
        sets: List<WorkoutSet>,
        modifier: Float,
        targetTimestamp: Long
    ): Map<BodyRegion, Float> {
        val statusMap = BodyRegion.entries.associateWith { 1.0f }.toMutableMap()

        sets.groupBy { it.region }.forEach { (region, regionSets) ->
            if (region == null) return@forEach

            val isLarge = (region == BodyRegion.CHEST || region == BodyRegion.BACK || region == BodyRegion.LEGS)
            val fullRecoveryHours = if (isLarge) RECOVERY_HOURS_LARGE else RECOVERY_HOURS_NORMAL

            var currentFatigue = 0.0f
            val sortedSets = regionSets.sortedBy { it.timestamp }

            sortedSets.forEachIndexed { index, set ->
                // 计算两组之间的疲劳恢复
                if (index > 0) {
                    val hours = (set.timestamp - sortedSets[index-1].timestamp).toFloat() / 3600000f
                    currentFatigue = applyNonLinearRecovery(currentFatigue, hours, fullRecoveryHours, modifier)
                }
                // RPE 5星扣除该部位单次上限的约 90%
                val drain = ((set.rpe ?: 1) / 5.0f) * (if (isLarge) 0.85f else 0.90f)
                currentFatigue = (currentFatigue + drain).coerceIn(0f, 1f)
            }

            // 计算从最后一组动作到当前时间点的恢复
            val lastActionTime = sortedSets.lastOrNull()?.timestamp ?: targetTimestamp
            val lastGap = (targetTimestamp - lastActionTime).toFloat() / 3600000f
            currentFatigue = applyNonLinearRecovery(currentFatigue, lastGap, fullRecoveryHours, modifier)

            statusMap[region] = (1.0f - currentFatigue).coerceIn(0f, 1f)
        }
        return statusMap
    }

    /**
     * 非线性恢复公式
     */
    private fun applyNonLinearRecovery(f: Float, h: Float, fullH: Float, m: Float): Float {
        if (f <= 0 || h <= 0) return f
        // 修正后的满血所需小时数
        val effectiveFullHours = fullH / m
        val progress = (h / effectiveFullHours).coerceIn(0f, 1f)
        // 疲劳 = 初始疲劳 * (1 - 进度)^k
        return (f * (1.0f - progress).toDouble().pow(RECOVERY_EXPONENT)).toFloat()
    }
}