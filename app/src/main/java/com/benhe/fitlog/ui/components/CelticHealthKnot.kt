package com.benhe.fitlog.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.model.DailyHealthStatus

@Composable
fun CelticHealthKnot(status: DailyHealthStatus) {
    // 1. 饮食判定: 80% - 110% 为健康
    val calorieRatio = if (status.calorieBurnTarget > 0) status.calorieIntake.toFloat() / status.calorieBurnTarget else 0f
    val isDietHealthy = calorieRatio in 0.8f..1.1f

    // 2. 运动判定: 恢复状态 < 710 为健康 (假设越低越好，或者根据你的逻辑调整)
    val isWorkoutHealthy = status.workoutFatigue < 710

    // 3. 睡眠判定: 平时>=7，大训练量>=8或8.5
    val sleepTarget = if (status.isHighTrainingLoad) 8.0f else 7.0f
    val isSleepHealthy = status.sleepHours >= sleepTarget

    // 颜色定义 (绿/红, 蓝/黄, 紫/灰)
    val dietColor = if (isDietHealthy) Color(0xFF4CAF50) else Color(0xFFFF5252)
    val workoutColor = if (isWorkoutHealthy) Color(0xFF2196F3) else Color(0xFFFFC107)
    val sleepColor = if (isSleepHealthy) Color(0xFF9C27B0) else Color(0xFF607D8B)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(180.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeW = 18.dp.toPx()
                val r = size.minDimension / 4
                val c = size.minDimension / 2

                // 绘制三个交错圆
                // 上(饮食)
                drawCircle(dietColor, r, Offset(c, c - r / 1.5f), style = Stroke(strokeW))
                // 左下(运动)
                drawCircle(workoutColor, r, Offset(c - r / 1.2f, c + r / 2), style = Stroke(strokeW))
                // 右下(睡眠)
                drawCircle(sleepColor, r, Offset(c + r / 1.2f, c + r / 2), style = Stroke(strokeW))
            }
            // 中心文字
            Text("核心状态", style = MaterialTheme.typography.labelMedium)
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LegendDot("饮食", dietColor)
            LegendDot("运动", workoutColor)
            LegendDot("睡眠", sleepColor)
        }
    }
}

@Composable
fun LegendDot(name: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(Modifier.width(4.dp))
        Text(name, fontSize = 12.sp)
    }
}