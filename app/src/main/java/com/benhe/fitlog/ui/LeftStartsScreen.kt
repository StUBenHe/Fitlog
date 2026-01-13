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
    onNavigateToWorkout: (String) -> Unit
) {
    val historyData by viewModel.bodyStatHistory.collectAsState()
    val calendarStatusMap by viewModel.calendarStatusMap.collectAsState()
    var selectedCalendarDate by remember { mutableStateOf(LocalDate.now()) }
    var showWeight by remember { mutableStateOf(true) }

    // ✅ 新增：控制跳转选择弹窗的显示状态
    var showSelectionDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. Apple 风格日历 ---
        AppleStyleCalendar(
            selectedDate = selectedCalendarDate,
            // ✅ 传入数据
            statusMap = calendarStatusMap,
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

    // ✅ 新增：日期操作选择弹窗
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

                    // 选项1：去饮食
                    Button(
                        onClick = {
                            showSelectionDialog = false
                            onNavigateToDiet(dateStr)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("饮食记录")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 选项2：去运动
                    Button(
                        onClick = {
                            showSelectionDialog = false
                            onNavigateToWorkout(dateStr)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("运动记录")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSelectionDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}