package com.benhe.fitlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.model.LifeIntensity
import kotlin.math.roundToInt

// 首页面板上的可点击卡片组件
@Composable
fun ExpandedModuleItem(title: String, mainValue: String, subItems: List<Pair<String, String>>, color: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(mainValue, fontWeight = FontWeight.Black, fontSize = 20.sp, color = Color(0xFFE67E22))
            }
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                subItems.forEach { (label, value) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(label, fontSize = 12.sp, color = Color.Gray)
                        Spacer(Modifier.height(4.dp))
                        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 记录睡眠和强度的弹窗
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityInputDialog(
    initialSleep: Float,
    initialIntensity: LifeIntensity,
    isAfterburnAutoActive: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Float, LifeIntensity) -> Unit
) {
    // 1. 初始化 sleep，确保是0.5的倍数
    var sleep by remember { mutableFloatStateOf((initialSleep * 2).roundToInt() / 2f) }
    var intensity by remember { mutableStateOf(initialIntensity) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📝 记录今日状态", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // --- 显示格式化后的时间 ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("睡眠时间")
                    Text(
                        text = "${String.format("%.1f", sleep)} 小时",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // --- Slider 步进逻辑 ---
                Slider(
                    value = sleep,
                    onValueChange = { newValue ->
                        // 核心逻辑：步长 0.5
                        sleep = (newValue * 2).roundToInt() / 2f
                    },
                    valueRange = 4f..12f,
                    steps = 15
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("4h", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("12h", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. 生活强度
                Text("生活强度: ${intensity.displayName}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    LifeIntensity.entries.forEach { item ->
                        val isSelected = intensity == item
                        FilterChip(
                            selected = isSelected,
                            onClick = { intensity = item },
                            label = { Text(item.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. 自动后燃效应展示
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isAfterburnAutoActive) Color(0xFFFFE0B2) else Color(0xFFF5F5F5),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAfterburnAutoActive) "🔥 后燃效应：已激活" else "❄️ 后燃效应：未开启",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isAfterburnAutoActive) Color(0xFFE65100) else Color.Gray
                        )
                        Text(
                            text = if (isAfterburnAutoActive) "检测到身体部分肌肉处于高疲劳状态" else "身体恢复良好，暂无额外后燃",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                    Icon(
                        imageVector = if (isAfterburnAutoActive) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isAfterburnAutoActive) Color(0xFFE65100) else Color.LightGray
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(sleep, intensity) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}