package com.benhe.fitlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.model.BodyRegion

/**
 * 身体各部位恢复状态的仪表盘视图
 */
@Composable
fun RecoveryDashboardView(
    regionStatus: Map<BodyRegion, Float>,
    modifier: Modifier = Modifier // 允许外部传入 modifier 以适应不同背景
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            // 默认给一个半透明白底和圆角，适应深色背景，外部可以通过 modifier 覆盖
            .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        regionStatus.forEach { (region, status) ->
            Row(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 部位名称
                Text(
                    text = region.displayName,
                    modifier = Modifier.width(50.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )

                // 进度条背景
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .background(Color(0xFFE0E0E0), CircleShape) // 灰底
                ) {
                    // 恢复进度条（根据状态变色）
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(status.coerceIn(0f, 1f)) // 确保在 0-1 之间
                            .fillMaxHeight()
                            .background(
                                when {
                                    status < 0.4f -> Color(0xFFFF5252) // 疲劳-红
                                    status < 0.8f -> Color(0xFF4A90E2) // 恢复中-蓝
                                    else -> Color(0xFF4CAF50) // 良好-绿
                                },
                                CircleShape
                            )
                    )
                }

                // 百分比文字
                Text(
                    text = "${(status * 100).toInt()}%",
                    modifier = Modifier.padding(start = 8.dp).width(35.dp),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}