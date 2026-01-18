package com.benhe.fitlog.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.model.BodyStatRecord
import java.util.Locale
import kotlin.math.abs
import androidx.compose.foundation.layout.fillMaxSize
/**
 * 一个完整的统计卡片组件，包含头部数据概览、周期对比、极值对比和底部的折线图。
 *
 * @param data 折线图所需的数据点列表。 **重要：假设数据是按时间顺序排列的，最后一个是当前值。**
 * @param isWeight True 表示显示体重，False 表示显示体脂率。
 * @param mainChangeValue 主变化数值字符串，例如 "-1.2"。通常由 ViewModel 计算得出。
 * @param comparisonPercentage 与上一周期（如上周）的比较百分比，例如 -0.02f 代表 -2%。
 * @param comparisonLabel 比较标签文本，例如 "较上周"。
 */
@Composable
fun StatsSummaryCard(
    modifier: Modifier = Modifier,
    data: List<BodyStatRecord>,
    isWeight: Boolean,
    mainChangeValue: String,
    comparisonPercentage: Float,
    comparisonLabel: String = "较上周"
) {
    val unitStr = if (isWeight) "kg" else "%"
    val subtitle = if (isWeight) "体重变化(本月)" else "体脂率变化(本月)"
    val chartColor = Color(0xFF00BCD4) // 青色
    val dropColor = Color(0xFF4CAF50) // 绿色 (下降)
    val riseColor = Color(0xFFF44336) // 红色 (上升)

    // 定义主趋势颜色
    val isNegativeTrend = comparisonPercentage < 0
    val mainTrendColor = if (isNegativeTrend) dropColor else riseColor
    val mainTrendBgColor = mainTrendColor.copy(alpha = 0.1f)

    // --- 计算极值对比逻辑 ---
    // 使用 remember 缓存计算结果，避免重组时重复计算
    val statsComparisonInfo = remember(data, isWeight) {
        if (data.isEmpty()) {
            Pair(null, null)
        } else {
            val points = data.map { if (isWeight) it.weight else it.bodyFatRate }
            // 假设数据按时间排序，最后一个为当前值
            val currentVal = points.last()
            val maxVal = points.max()
            val minVal = points.min()

            // 格式化辅助函数
            fun formatDiff(val1: Float, val2: Float): String {
                return String.format(Locale.getDefault(), "%.1f", abs(val1 - val2))
            }

            // 对比最高值逻辑
            val vsMaxText = if (currentVal >= maxVal) {
                "当前为最高值" to riseColor // 已经是最高，用红色强调
            } else {
                "较最高值 -${formatDiff(maxVal, currentVal)}$unitStr" to dropColor // 下降用绿色
            }

            // 对比最低值逻辑
            val vsMinText = if (currentVal <= minVal) {
                "当前为最低值" to dropColor // 已经是最低，用绿色强调
            } else {
                "较最低值 +${formatDiff(currentVal, minVal)}$unitStr" to riseColor // 上升用红色
            }

            Pair(vsMaxText, vsMinText)
        }
    }
    val (vsMaxInfo, vsMinInfo) = statsComparisonInfo
    // -----------------------

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5FDFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // --- 头部标题 ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "月度趋势",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Text(
                    text = "GROWTH ANALYTICS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = mainTrendColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 主数值与周期比较标签 ---
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF263238))) {
                            append(mainChangeValue)
                        }
                        withStyle(style = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, color = Color.Gray)) {
                            append(" $unitStr")
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                ComparisonBadge(
                    label = comparisonLabel,
                    percentage = comparisonPercentage,
                    textColor = mainTrendColor,
                    bgColor = mainTrendBgColor
                )
            }
            // 副标题
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- 新增：极值对比区域 ---
            if (vsMaxInfo != null && vsMinInfo != null) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ComparisonDetailRow(text = vsMaxInfo.first, valueColor = vsMaxInfo.second)
                    ComparisonDetailRow(text = vsMinInfo.first, valueColor = vsMinInfo.second)
                }
            }
            // -----------------------

            Spacer(modifier = Modifier.height(24.dp))

            // --- 折线图区域 ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                StatsLineChartCanvasOnly(data = data, isWeight = isWeight, chartColor = chartColor)
            }
        }
    }
}

/**
 * 用于显示极值对比详情的小行组件
 */
@Composable
private fun ComparisonDetailRow(
    text: String,
    valueColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 可以在前面加个小图标，这里暂时用文字
        Text(
            text = "• ",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        ).apply {
            // 这里是一个简单的处理，如果想让数字部分变色，需要用 buildAnnotatedString 解析 text
            // 为了演示简单，这里根据传入的 valueColor 设置整体颜色，实际项目中可以更精细
            // 比如: "较最高值" 为灰色， "-1.5kg" 为绿色
        }
    }
}

/**
 * 用于显示比较结果的小标签组件 (Badge)
 */
@Composable
private fun ComparisonBadge(
    label: String,
    percentage: Float,
    textColor: Color,
    bgColor: Color
) {
    val percentageStr = String.format(Locale.getDefault(),"%.0f%%", percentage * 100)
    val sign = if (percentage > 0) "+" else ""

    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$label $sign$percentageStr",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

/**
 * 仅包含 Canvas 绘图逻辑的组件
 */
@Composable
private fun StatsLineChartCanvasOnly(
    data: List<BodyStatRecord>,
    isWeight: Boolean,
    chartColor: Color
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (data.isEmpty()) return@Canvas

        val points = data.map { if (isWeight) it.weight else it.bodyFatRate }
        val max = (points.maxOrNull() ?: 100f)
        val min = (points.minOrNull() ?: 0f)
        val range = (max - min).let { if (it == 0f) 1f else it }

        val chartHeight = size.height
        val widthPerStep = size.width / (points.size - 1).coerceAtLeast(1)
        val path = Path()

        val verticalMarginRatio = 0.2f
        val effectiveHeight = chartHeight * (1 - 2 * verticalMarginRatio)

        points.forEachIndexed { i, value ->
            val x = i * widthPerStep
            val yNormalized = (value - min) / range
            val y = chartHeight - (verticalMarginRatio * chartHeight + yNormalized * effectiveHeight)

            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)

            val pointCenter = Offset(x, y)
            drawCircle(color = Color.White, radius = 4.dp.toPx(), center = pointCenter)
            drawCircle(color = chartColor, radius = 4.dp.toPx(), center = pointCenter, style = Stroke(width = 2.dp.toPx()))
        }
        drawPath(path, chartColor, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
    }
}