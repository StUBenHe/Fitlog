package com.benhe.fitlog.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


// ✅ 已删除 data class DayHealthStatus
val PeriodRed = Color(0xFFE91E63) // 深红色，表示已发生
val PeriodPink = Color(0xFFF48FB1) // 粉红色，表示预测

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppleStyleCalendar(
    selectedDate: LocalDate,
    // ✅ 新增参数：已发生的经期日期集合
    pastPeriodDates: Set<LocalDate> = emptySet(),
    // ✅ 新增参数：预测的经期日期集合
    predictedPeriodDates: Set<LocalDate> = emptySet(),
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
                    // ✅ 已删除获取 status 的逻辑
                    val isPastPeriod = pastPeriodDates.contains(date)
                    val isPredictedPeriod = predictedPeriodDates.contains(date)
                    DayCell(
                        date = date,
                        isSelected = date == selectedDate,
                        // ✅ 传递状态给 DayCell
                        isPastPeriod = isPastPeriod,
                        isPredictedPeriod = isPredictedPeriod,
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
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "上个月")
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy年 MM月")),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onNextClick) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "下个月")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    // ✅ 已删除 status: DayHealthStatus 参数
    isPastPeriod: Boolean,
    isPredictedPeriod: Boolean,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()
    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        isPastPeriod -> PeriodRed.copy(alpha = 0.8f) // 已发生：深红
        isPredictedPeriod -> PeriodPink.copy(alpha = 0.6f) // 预测：浅粉
        else -> Color.Transparent
    }
    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isPastPeriod || isPredictedPeriod -> Color.White // 如果是经期背景，文字变白
        isToday -> MaterialTheme.colorScheme.primary
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            // ✅ 使用计算出的背景色
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // 1. 日期数字
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 14.sp,
            // ✅ 使用计算出的文字颜色和粗细
            fontWeight = if (isSelected || isToday || isPastPeriod) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )

        // 2. 今天的小圆点标记 (如果今天是经期，可能需要隐藏这个点，或者改变颜色，这里暂且保留)
        if (isToday && !isPastPeriod && !isPredictedPeriod) {
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

