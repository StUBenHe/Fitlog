package com.benhe.fitlog.ui

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.benhe.fitlog.model.LifeIntensity
// ✅ 导入需要的组件和工具类
import com.benhe.fitlog.ui.components.ActivityInputDialog
import com.benhe.fitlog.ui.components.AppleStyleCalendar
import com.benhe.fitlog.ui.components.StatsLineChart

import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeftStatsScreen(
    viewModel: MainViewModel,
    onNavigateToDiet: (String) -> Unit,
    onNavigateToWorkout: (String) -> Unit,
    // 注意：这里不再需要 onNavigateToStatus 回调了，因为我们在内部处理
) {
    val historyData by viewModel.bodyStatHistory.collectAsState()
    var selectedCalendarDate by remember { mutableStateOf(LocalDate.now()) }
    var showWeight by remember { mutableStateOf(true) }

    // 控制第一个弹窗（三按钮菜单）显示的状态
    var showSelectionDialog by remember { mutableStateOf(false) }

    // ✅ 新增：控制第二个弹窗（记录状态）显示的状态
    var showActivityDialog by remember { mutableStateOf(false) }

    // ✅ 获取后燃效应自动开启状态
    val isAfterburnAuto by viewModel.isAfterburnAutoActive.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (AppleStyleCalendar 和 StatsLineChart 部分保持不变) ...
        AppleStyleCalendar(
            selectedDate = selectedCalendarDate,
            onDateSelected = { date ->
                selectedCalendarDate = date
                showSelectionDialog = true
            }
        )
        Spacer(Modifier.height(24.dp))
        Divider()
        Spacer(Modifier.height(24.dp))

        // --- 2. 底部：折线统计图 ---
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
                    modifier = Modifier.scale(0.8f)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            if (historyData.isNotEmpty()) {
                StatsLineChart(data = historyData, isWeight = showWeight)
            } else {
                Text(
                    text = "暂无数据，请在'状态'页记录体重",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(80.dp))
    }

    // --- 第一个弹窗：三按钮菜单 ---
    if (showSelectionDialog) {
        val dateStr = selectedCalendarDate.toString() // yyyy-MM-dd
        val titleDate = selectedCalendarDate.format(DateTimeFormatter.ofPattern("MM月dd日"))

        AlertDialog(
            onDismissRequest = { showSelectionDialog = false },
            title = { Text(text = "$titleDate 记录") },
            text = {
                Column {
                    Text("请选择要查看或记录的内容：")
                    Spacer(modifier = Modifier.height(16.dp))
                    // ... 按钮1：饮食记录 (保持不变) ...
                    Button(onClick = { showSelectionDialog = false; onNavigateToDiet(dateStr) }, modifier = Modifier.fillMaxWidth()) { Text("饮食记录") }
                    Spacer(modifier = Modifier.height(8.dp))
                    // ... 按钮2：运动记录 (保持不变) ...
                    Button(onClick = { showSelectionDialog = false; onNavigateToWorkout(dateStr) }, modifier = Modifier.fillMaxWidth()) { Text("运动记录") }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ✅ 按钮 3：修改点击事件
                    Button(
                        onClick = {
                            showSelectionDialog = false // 关闭第一个弹窗
                            showActivityDialog = true   // 打开第二个弹窗
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors()
                    ) {
                        Text("记录今日状态")
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showSelectionDialog = false }) { Text("取消") } }
        )
    }

    // ✅ --- 第二个弹窗：使用 ActivityInputDialog ---
    if (showActivityDialog) {
        val dateStr = selectedCalendarDate.toString()
        // 获取所选日期的活动数据，如果没有则为 null
        val activityData = viewModel.getActivityForDate(dateStr).collectAsState(initial = null).value

        // ✅ 调用您现有的 ActivityInputDialog 组件
        ActivityInputDialog(
            // 如果有历史数据就使用，没有就用默认值 (例如 8小时睡眠，正常强度)
            initialSleep = activityData?.sleepHours ?: 8f,
            initialIntensity = activityData?.intensity ?: LifeIntensity.NORMAL,
            isAfterburnAutoActive = isAfterburnAuto,
            onDismiss = { showActivityDialog = false },
            onConfirm = { sleep, intensity ->
                // 调用 ViewModel 保存数据
                viewModel.onActivityConfirm(dateStr, sleep, intensity)
                showActivityDialog = false
            }
        )
    }
}