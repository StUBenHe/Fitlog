package com.benhe.fitlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.model.BodyRegion

// --- 定义渐变色和背景色 ---
val BlueGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF7F9CF5), Color(0xFF63B3ED))
)
val CyanGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF4FD1C5), Color(0xFF38B2AC))
)
val MixedGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF4FD1C5), Color(0xFF7F9CF5))
)

// 进度条底色 (为了在新背景上清晰，使用半透明灰色)
val ProgressTrackColor = Color(0xFFE0E0E0).copy(alpha = 0.5f)

// 标题图标小方块的背景色 (原来的淡紫色，保留用于对比)
val TitleIconBg = Color(0xFFEDE7F6)

// 【新增】整个大卡片的背景色：极淡的、接近白色的紫色
val MainCardBg = Color(0xFFF8F7FC)

val TitleIconTint = Color(0xFF9575CD)

/**
 * 身体各部位恢复状态的仪表盘视图 (已排序，极淡紫色背景)
 */
@Composable
fun RecoveryDashboardView(
    regionStatus: Map<BodyRegion, Float>,
    modifier: Modifier = Modifier
) {
    // 排序逻辑保持不变
    val sortedStatusList = remember(regionStatus) {
        regionStatus.toList().sortedBy { it.second }
    }

    // --- 最外层卡片容器 ---
    Box(
        modifier = modifier
            .fillMaxWidth()
            // 【改动点】背景色改为新的极淡紫色 MainCardBg
            .background(MainCardBg, RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        Column {
            // --- 标题区域 ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        // 图标小方块依然使用原来的淡紫色，形成层次感
                        .background(TitleIconBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AccessibilityNew,
                        contentDescription = null,
                        tint = TitleIconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "恢复情况",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(24.dp))

            // --- 列表循环 (保持不变) ---
            sortedStatusList.forEach { (region, status) ->
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    // 文字信息栏
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = region.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${(status * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // 进度条
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            // 进度条背景色
                            .background(ProgressTrackColor, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(status.coerceIn(0f, 1f))
                                .fillMaxHeight()
                                .background(
                                    brush = when {
                                        status < 0.5f -> CyanGradient
                                        status < 0.8f -> MixedGradient
                                        else -> BlueGradient
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}