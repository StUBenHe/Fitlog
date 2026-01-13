package com.benhe.fitlog.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi // 用于 Pager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.model.BodyStatRecord
import com.benhe.fitlog.model.DailyHealthStatus
import com.benhe.fitlog.ui.components.AppleStyleCalendar // ✅ 导入新的日历
import com.benhe.fitlog.ui.components.CelticHealthKnot
import com.benhe.fitlog.ui.components.StatsLineChart
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeftStatsScreen(
    viewModel: MainViewModel,
    onNavigateToDiet: (String) -> Unit,
    onNavigateToWorkout: (String) -> Unit
) {
    // 1. 获取健康状态数据 (用于凯尔特结)
    // 实际应该从 viewModel 获取 DailyHealthStatus，这里为了编译先用假数据
    val healthStatus = DailyHealthStatus(
        calorieIntake = 2100,
        calorieBurnTarget = 2200,
        workoutFatigue = 600, // 假设0-1000
        sleepHours = 7.5f,
        isHighTrainingLoad = false
    )

    // 2. 获取历史身体数据 (用于折线图)
    val historyData by viewModel.bodyStatHistory.collectAsState()

    // 3. 管理日历选中日期 (这里可以与 MainScreen 的 selectedDate 状态保持一致)
    var selectedCalendarDate by remember { mutableStateOf(LocalDate.now()) }

    var showWeight by remember { mutableStateOf(true) } // 折线图切换状态

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. 顶部：凯尔特结健康状态 ---
        Text("今日身体状态", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        CelticHealthKnot(status = healthStatus) // 传入健康状态

        Spacer(Modifier.height(24.dp))
        Divider()
        Spacer(Modifier.height(16.dp))

        // --- 2. 中间：Apple 风格日历 ---
        Text("训练日程", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        AppleStyleCalendar(
            selectedDate = selectedCalendarDate,
            onDateSelected = { date ->
                selectedCalendarDate = date
                // 这里可以根据需求，点击日期后，是否要跳转到对应的饮食或运动详情
                // onNavigateToDiet(date.toString()) // 如果点击日期要直接跳转到饮食
                // onNavigateToWorkout(date.toString()) // 如果点击日期要直接跳转到运动
            }
        )

        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(16.dp))

        // --- 3. 底部：折线统计图 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("趋势分析", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (showWeight) "体重(kg)" else "体脂(%)", style = MaterialTheme.typography.bodySmall)
                Switch(
                    checked = !showWeight,
                    onCheckedChange = { showWeight = !it },
                    modifier = Modifier.scale(0.8f) // 修复 scale 报错
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            if (historyData.isNotEmpty()) {
                StatsLineChart(data = historyData, isWeight = showWeight) // 传入真实的 ViewModel 数据
            } else {
                Text(
                    text = "暂无身体数据记录",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            }
        }

        // 底部留白，防止被导航栏遮挡
        Spacer(Modifier.height(80.dp))
    }
}

// ✅ 重要的： MiniCalendarSection 已经被 AppleStyleCalendar 替换了，所以删掉 MiniCalendarSection 的定义。
// 如果你仍需要 MiniCalendarSection，请重新思考它的用途或将其独立为其他组件。