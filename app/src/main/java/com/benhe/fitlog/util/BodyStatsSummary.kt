package com.benhe.fitlog.util

import com.benhe.fitlog.model.BodyStatRecord
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * 用于保存计算后的体重/体脂统计摘要的数据类
 *
 * @param mainChangeValue 主变化数值字符串，例如 "+1.5" 或 "-0.8"。
 * @param comparisonPercentage 比较百分比，例如 -0.02 代表 -2% (上升为正，下降为负)。
 * @param comparisonLabel 比较标签，说明对比的是哪个周期，例如 "较30天前"。
 */
data class BodyStatsSummary(
    val mainChangeValue: String = "0.0",
    val comparisonPercentage: Float = 0f,
    val comparisonLabel: String = "暂无对比"
)

/**
 * 用于计算身体指标统计数据的工具类对象
 */
object BodyStatsCalculator {

    // 日期格式化器。注意：这里假设你的数据库里存的日期是 yyyy-MM-dd 格式。
    // 如果你的格式不一样（例如 MM-dd），你需要在这里修改。
    private const val DATE_PATTERN = "yyyy-MM-dd"
    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

    /**
     * 计算最新数据与大约 30 天前数据的对比摘要。
     *
     * @param allRecords 所有历史记录列表，**必须按日期升序排列**。
     * @param isWeight True 表示计算体重，False 表示计算体脂率。
     * @param daysToCompare 对比多少天前的数据，默认为 30 天。
     * @return 计算好的 [BodyStatsSummary] 对象。
     */
    fun calculateSummary(
        allRecords: List<BodyStatRecord>,
        isWeight: Boolean,
        daysToCompare: Long = 30
    ): BodyStatsSummary {
        // 1. 基础检查：如果记录少于2条，无法进行对比
        if (allRecords.size < 2) {
            return BodyStatsSummary(comparisonLabel = "数据不足")
        }

        try {
            // 2. 获取最新的记录 (列表最后一条)
            val latestRecord = allRecords.last()
            val latestDate = parseDate(latestRecord.dateString)

            // 3. 确定目标基准日期 (例如：30天前)
            val targetDate = latestDate.minusDays(daysToCompare)

            // 4. 寻找基准记录
            // 策略：从后往前找，找到第一个日期小于或等于目标日期的记录。
            // 这样可以找到最接近且不晚于30天前的记录。
            var baselineRecord: BodyStatRecord? = null
            for (i in allRecords.lastIndex - 1 downTo 0) {
                val record = allRecords[i]
                val recordDate = parseDate(record.dateString)
                if (recordDate <= targetDate) {
                    baselineRecord = record
                    break
                }
            }

            // 如果没找到符合条件的（说明所有历史数据都在最近30天内），就退而求其次，用第一条记录做基准
            val finalBaselineRecord = baselineRecord ?: allRecords.first()

            // 极端情况防御：如果最新的和基准的是同一条，说明只有一条有效数据
            if (latestRecord == finalBaselineRecord) {
                return BodyStatsSummary(comparisonLabel = "数据不足")
            }

            // 5. 获取数值进行计算
            val latestValue = getValue(latestRecord, isWeight)
            val baselineValue = getValue(finalBaselineRecord, isWeight)

            // 计算绝对变化量
            val change = latestValue - baselineValue
            // 格式化字符串：保留1位小数，强制显示正负号 (例如 "+1.5", "-0.8", "+0.0")
            val mainChangeStr = String.format(Locale.getDefault(), "%+.1f", change)

            // 计算相对百分比 (避免除以零错误)
            val percentage = if (baselineValue != 0f) {
                (latestValue - baselineValue) / baselineValue
            } else {
                0f
            }

            // 计算实际相差的天数，用于显示动态标签
            val baselineDate = parseDate(finalBaselineRecord.dateString)
            val actualDaysDiff = ChronoUnit.DAYS.between(baselineDate, latestDate)
            // 如果天数差距很小，显示具体天数；如果接近30天，也可以显示“较上月”等固定文案
            val label = "较${actualDaysDiff}天前"

            return BodyStatsSummary(
                mainChangeValue = mainChangeStr,
                comparisonPercentage = percentage,
                comparisonLabel = label
            )

        } catch (e: Exception) {
            // 捕获可能的日期解析异常或其他运行时异常，避免应用崩溃
            e.printStackTrace()
            // 在日志中输出错误，并在UI上显示一个错误状态提示
            return BodyStatsSummary(mainChangeValue = "--", comparisonLabel = "数据格式错误")
        }
    }

    // 辅助函数：安全解析日期
    private fun parseDate(dateString: String): LocalDate {
        // 如果这里报错，请检查你的 BodyStatRecord.dateString 的实际格式是否和 DATE_PATTERN 一致
        return LocalDate.parse(dateString, dateFormatter)
    }

    // 辅助函数：根据标志位取值
    private fun getValue(record: BodyStatRecord, isWeight: Boolean): Float {
        return if (isWeight) record.weight else record.bodyFatRate
    }
}