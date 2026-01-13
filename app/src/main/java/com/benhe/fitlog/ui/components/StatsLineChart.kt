package com.benhe.fitlog.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.model.BodyStatRecord

@Composable
fun StatsLineChart(
    data: List<BodyStatRecord>,
    isWeight: Boolean
) {
    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.GRAY
        textSize = 30f
        textAlign = android.graphics.Paint.Align.CENTER
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        if (data.isEmpty()) return@Canvas

        // 提取数值
        val points = data.map { if (isWeight) it.weight else it.bodyFatRate }

        val max = (points.maxOrNull() ?: 100f) * 1.05f
        val min = (points.minOrNull() ?: 0f) * 0.95f
        val range = (max - min).coerceAtLeast(1f)

        // 留出底部空间画文字
        val chartHeight = size.height - 50f
        val widthPerStep = size.width / (points.size - 1).coerceAtLeast(1)
        val path = Path()

        points.forEachIndexed { i, value ->
            val x = i * widthPerStep
            val y = chartHeight - ((value - min) / range * chartHeight)

            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)

            drawCircle(
                color = Color(0xFF1976D2),
                radius = 4.dp.toPx(),
                center = Offset(x, y)
            )

            // ✅ 绘制横坐标 (日期)
            // 为了防止拥挤，如果数据量大，每隔几个点画一次
            val step = if (data.size > 10) 2 else 1
            if (i % step == 0) {
                drawContext.canvas.nativeCanvas.drawText(
                    data[i].dateString, // 这里用数据库存的 MM-dd
                    x,
                    size.height, // 画在最底部
                    textPaint
                )
            }
        }
        drawPath(path, Color(0xFF1976D2), style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
    }
}