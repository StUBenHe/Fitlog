package com.benhe.fitlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.viewmodel.MainViewModel

@Composable
fun DayCard(
    date: String,
    weekday: String,
    isToday: Boolean,
    viewModel: MainViewModel,
    onDietClick: () -> Unit
) {
    val totalCalories by viewModel.getTotalCaloriesForDate(date).collectAsState(initial = 0.0)
    val totalProtein by viewModel.getTotalProteinForDate(date).collectAsState(initial = 0.0)
    val totalCarbs by viewModel.getTotalCarbsForDate(date).collectAsState(initial = 0.0)
    val isAfterburnAuto by viewModel.isAfterburnAutoActive.collectAsState()
    val activityState = viewModel.getActivityForDate(date).collectAsState(initial = null)
    val activityData = activityState.value
    val tdee = viewModel.getTodayExpenditure(activityData)

    val bodyStatus by viewModel.bodyStatus.collectAsState()

    var showActivityDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = if (isToday) Color(0xFFE0E7FF) else Color(0xFFF3F4F6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部日期显示
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = weekday, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = date, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(24.dp))

            // 1. 状态卡片 (Status)
            ExpandedModuleItem(
                title = "🏃 Status",
                mainValue = if (activityData != null) "${activityData.sleepHours}h" else "待记录",
                subItems = listOf(
                    "睡眠" to "${activityData?.sleepHours ?: "--"}h",
                    "强度" to (activityData?.intensity?.displayName ?: "未设置"),
                    "消耗" to "${tdee} kcal"
                ),
                color = Color(0xFFF0FDF4), // 浅绿色背景
                onClick = { showActivityDialog = true }
            )

            Spacer(Modifier.height(12.dp))

            // 2. 饮食卡片 (Diet)
            ExpandedModuleItem(
                title = "🍽 Diet",
                mainValue = if (totalCalories > 0) "${totalCalories.toInt()} kcal" else "点击记录",
                subItems = listOf(
                    "蛋白质" to "${totalProtein.toInt()}g",
                    "碳水" to "${totalCarbs.toInt()}g",
                    "脂肪" to "待计算"
                ),
                color = Color(0xFFFFF7ED), // 浅橙色背景
                onClick = onDietClick
            )

            Spacer(Modifier.height(16.dp))

            // 🏆 身体状态看板
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text("Body Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                // 使用提取出来的通用组件
                RecoveryDashboardView(regionStatus = bodyStatus, modifier = Modifier)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
    if (showActivityDialog) {
        // 使用提取出来的通用组件
        ActivityInputDialog(
            initialSleep = activityData?.sleepHours ?: 8f,
            initialIntensity = activityData?.intensity ?: LifeIntensity.NORMAL,
            isAfterburnAutoActive = isAfterburnAuto,
            onDismiss = { showActivityDialog = false },
            onConfirm = { sleep, intensity ->
                viewModel.onActivityConfirm(date, sleep, intensity)
                showActivityDialog = false
            }
        )
    }
}