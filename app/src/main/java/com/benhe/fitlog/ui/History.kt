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
// ✅ 【非常重要】确保包含以下两个导入，它们解决了 "Property delegate must have a getValue" 和 "Cannot infer type" 的错误
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.ui.components.ActivityInputDialog
import com.benhe.fitlog.ui.components.AppleStyleCalendar
import com.benhe.fitlog.ui.components.StatsLineChart
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// 假设您在主题文件中定义了深红色，如果没有，可以使用 Color(0xFFE91E63)
val PeriodRedColor = Color(0xFFE91E63)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun History( // 假设您可能把 LeftStatsScreen 改名成了 HistoryScreen，如果没改请用回原来的名字
    viewModel: MainViewModel,
    onNavigateToDiet: (String) -> Unit,
    onNavigateToWorkout: (String) -> Unit
) {
    // 监听体重/体脂历史数据
    val historyData by viewModel.bodyStatHistory.collectAsState()

    // ✅ 【修复核心】监听 ViewModel 中的经期数据流
    // 如果这里报错，请确保您完成了“第一步：重建项目”
    val pastPeriodDates by viewModel.pastPeriodDates.collectAsState(initial = emptySet())
    val predictedPeriodDates by viewModel.predictedPeriodDates.collectAsState(initial = emptySet())

    // UI 状态
    var selectedCalendarDate by remember { mutableStateOf(LocalDate.now()) }
    var showWeight by remember { mutableStateOf(true) }
    var showSelectionDialog by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }

    // 获取后燃效应自动开启状态
    val isAfterburnAuto by viewModel.isAfterburnAutoActive.collectAsState()

    // ✅ 新增状态：判断当前选中的日期是否已经被标记为经期（用于更新按钮文字）
    var isCurrentDateMarkedAsPeriod by remember { mutableStateOf(false) }

    // 监听选中日期的变化或历史数据的变化，实时更新按钮状态
    LaunchedEffect(selectedCalendarDate, pastPeriodDates) {
        isCurrentDateMarkedAsPeriod = pastPeriodDates.contains(selectedCalendarDate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. 日历组件 ---
        AppleStyleCalendar(
            selectedDate = selectedCalendarDate,
            // ✅ 将数据传递给日历组件进行渲染
            pastPeriodDates = pastPeriodDates,
            predictedPeriodDates = predictedPeriodDates,
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

    // --- 第一个弹窗：操作选择菜单 ---
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

                    // ✅ 【核心功能】新增：经期标记按钮
                    Button(
                        onClick = {
                            // ✅ 调用 ViewModel 的方法切换状态。如果报错，请确保重建了项目。
                            viewModel.togglePeriodStatus(selectedCalendarDate)
                            // 可选：点击后关闭弹窗，或者保留以便进行其他操作
                            showSelectionDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        // 根据当前状态改变按钮颜色：已标记显示灰色(取消)，未标记显示红色(标记)
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCurrentDateMarkedAsPeriod) Color.Gray else PeriodRedColor
                        )
                    ) {
                        Text(if (isCurrentDateMarkedAsPeriod) "取消经期标记" else "标记为经期日")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    // 按钮：饮食记录
                    Button(onClick = { showSelectionDialog = false; onNavigateToDiet(dateStr) }, modifier = Modifier.fillMaxWidth()) { Text("饮食记录") }
                    Spacer(modifier = Modifier.height(8.dp))
                    // 按钮：运动记录
                    Button(onClick = { showSelectionDialog = false; onNavigateToWorkout(dateStr) }, modifier = Modifier.fillMaxWidth()) { Text("运动记录") }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 按钮：记录状态
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

    // --- 第二个弹窗：状态录入 (保持不变) ---
    if (showActivityDialog) {
        val dateStr = selectedCalendarDate.toString()
        // 获取所选日期的活动数据，如果没有则为 null
        val activityData = viewModel.getActivityForDate(dateStr).collectAsState(initial = null).value

        ActivityInputDialog(
            // 如果有历史数据就使用，没有就用默认值
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