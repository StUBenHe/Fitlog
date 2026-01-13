package com.benhe.fitlog.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft // ✅ 修改了这里
import androidx.compose.material.icons.filled.KeyboardArrowRight // ✅ 修改了这里
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.random.Random

// 数据模型
data class DayHealthStatus(
    val isDietGoalMet: Boolean,
    val isWorkoutGoalMet: Boolean,
    val isSleepGoalMet: Boolean
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppleStyleCalendar(
    statusMap: Map<String, DayHealthStatus>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // 状态：当前展示的月份
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    // 计算当月的数据
    val days = remember(currentMonth) {
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfWeekVal = currentMonth.atDay(1).dayOfWeek.value
        // 将周一(1)..周日(7) 转换为日历习惯的 周日(0)..周六(6)
        val emptySlots = if (firstDayOfWeekVal == 7) 0 else firstDayOfWeekVal

        val list = mutableListOf<LocalDate?>()
        repeat(emptySlots) { list.add(null) }
        for (i in 1..daysInMonth) {
            list.add(currentMonth.atDay(i))
        }
        list
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp)
    ) {
        // --- 1. 顶部月份切换 ---
        CalendarHeader(
            currentMonth = currentMonth,
            onPreviousClick = { currentMonth = currentMonth.minusMonths(1) },
            onNextClick = { currentMonth = currentMonth.plusMonths(1) }
        )

        // --- 2. 星期标题 ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val weekDays = listOf("日", "一", "二", "三", "四", "五", "六")
            weekDays.forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // --- 3. 日期网格 ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days) { date ->
                if (date != null) {
                    val dateStr = date.toString() // "yyyy-MM-dd"
                    val status = statusMap[dateStr] ?: DayHealthStatus(false, false, false)

                    DayCell(
                        date = date,
                        isSelected = date == selectedDate,
                        status = status,
                        onClick = { onDateSelected(date) }
                    )
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            // ✅ 修改了这里：去掉了 AutoMirrored，使用通用的 Default/Filled
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "上个月")
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy年 MM月")),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onNextClick) {
            // ✅ 修改了这里
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "下个月")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    status: DayHealthStatus,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // 1. 绘制凯尔特结印章
        TinyCelticKnotStamp(
            isDietGood = status.isDietGoalMet,
            isWorkoutGood = status.isWorkoutGoalMet,
            isSleepGood = status.isSleepGoalMet,
            modifier = Modifier.fillMaxSize(0.85f)
        )

        // 2. 日期数字
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 14.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black
        )

        // 今天的小圆点标记
        if (isToday) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
                    .size(4.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
    }
}

@Composable
fun TinyCelticKnotStamp(
    isDietGood: Boolean,
    isWorkoutGood: Boolean,
    isSleepGood: Boolean,
    modifier: Modifier = Modifier
) {
    // ✅ 修改点：把 inactiveColor (未达标颜色) 调深一点，从 EEEEEE 改为 D0D0D0
    // 这样即使没有数据，你也能看到三个灰色的空圈圈
    val inactiveColor = Color(0xFFD0D0D0)

    val dietColor = if (isDietGood) Color(0xFF4CAF50) else inactiveColor // 绿
    val workoutColor = if (isWorkoutGood) Color(0xFF2196F3) else inactiveColor // 蓝
    val sleepColor = if (isSleepGood) Color(0xFF9C27B0) else inactiveColor // 紫

    Canvas(modifier = modifier) {
        val strokeWidth = 3.dp.toPx()
        val radius = size.minDimension / 3.8f
        val centerX = size.width / 2
        val centerY = size.height / 2

        val offsetY = radius * 0.6f
        val offsetX = radius * 0.5f

        // 1. 上圆 (饮食)
        drawCircle(
            color = dietColor,
            radius = radius,
            center = Offset(centerX, centerY - offsetY),
            style = Stroke(width = strokeWidth)
        )

        // 2. 左下圆 (运动)
        drawCircle(
            color = workoutColor,
            radius = radius,
            center = Offset(centerX - offsetX * 1.5f, centerY + offsetY),
            style = Stroke(width = strokeWidth)
        )

        // 3. 右下圆 (睡眠)
        drawCircle(
            color = sleepColor,
            radius = radius,
            center = Offset(centerX + offsetX * 1.5f, centerY + offsetY),
            style = Stroke(width = strokeWidth)
        )
    }
}


