package com.benhe.fitlog.ui.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.model.DailyActivity
import com.benhe.fitlog.model.LifeIntensity

@Composable
fun ActivityCard(
    activity: DailyActivity?,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onEditClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F7FF)), // 浅蓝色
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏃 每日状态",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                val recoveryText = if ((activity?.sleepHours ?: 0f) >= 7f) "恢复良好" else "建议多睡"
                Text(text = recoveryText, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("睡眠时长", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        text = "${activity?.sleepHours ?: 8.0} h",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("生活强度", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        text = activity?.intensity?.displayName ?: "正常",
                        color = Color(activity?.intensity?.color ?: 0xFF4CAF50),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityInputDialog(
    initialSleep: Float,
    initialIntensity: LifeIntensity,
    initialAfterburn: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Float, LifeIntensity, Boolean) -> Unit
) {
    var sleep by remember { mutableStateOf(initialSleep) }
    var intensity by remember { mutableStateOf(initialIntensity) }
    var afterburn by remember { mutableStateOf(initialAfterburn) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("记录今日状态") },
        text = {
            Column {
                Text("睡眠时间: ${String.format("%.1f", sleep)} 小时", fontWeight = FontWeight.Bold)
                // ✅ 调整 Slider：Range 4-12, 0.5步进对应 15 个台阶
                Slider(
                    value = sleep,
                    onValueChange = { sleep = it },
                    valueRange = 4f..12f,
                    steps = 15 // (12-4)/0.5 - 1 = 15 个间断点
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("生活强度:", fontWeight = FontWeight.Bold)
                LifeIntensity.entries.forEach { level ->
                    Row(
                        Modifier.fillMaxWidth().selectable(
                            selected = (level == intensity),
                            onClick = { intensity = level }
                        ).padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (level == intensity), onClick = { intensity = level })
                        Text(level.displayName, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ 后燃效应开关
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("后燃效应", style = MaterialTheme.typography.bodyLarge)
                        Text("高强度运动后代谢提升10%", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(checked = afterburn, onCheckedChange = { afterburn = it })
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(sleep, intensity, afterburn) }) { Text("确定") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}