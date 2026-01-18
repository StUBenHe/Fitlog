package com.benhe.fitlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.viewmodel.MainViewModel
// 确保你的 Color.kt 有这些定义
// --- 背景色系 ---
val BgMain = Color(0xFFF0F5F3)       // 全局背景(淡青灰)
val BgCardTop = Color(0xFFEDE7F6)    // 顶部紫色卡片背景
val BgCardStatus = Color(0xFFF1F8E9) // Status 绿色卡片背景
val BgCardDiet = Color(0xFFFFF3E0)   // Diet 橙色卡片背景

// --- 强调色系 ---
val AccentOrange = Color(0xFFFF9800) // 橙色强调色
val AccentGreen = Color(0xFF4CAF50)  // 绿色强调色

// --- 文字色系 ---
val TextPrimary = Color(0xFF2D2D2D)   // 主要文字 (深灰/近黑)
val TextSecondary = Color(0xFF757575)

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

    // 简单估算脂肪量
    val totalFat = ((totalCalories - (totalProtein * 4) - (totalCarbs * 4)) / 9).coerceAtLeast(0.0)

    // 【改动 1】移除最外层的白色大 Card，直接使用 Column 布局
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            // 如果需要，可以在这里添加 padding，让卡片之间有间距
            // .padding(vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 【改动 1】全新的独立日期标题卡片
        DateHeaderCard(weekday = weekday, date = date, isToday = isToday)

        Spacer(Modifier.height(16.dp))

        // --- 状态卡片 ---
        StatusCard(
            activityData = activityData,
            tdee = tdee,
            onClick = { showActivityDialog = true }
        )

        Spacer(Modifier.height(16.dp))

        // --- 饮食卡片 ---
        DietCard(
            totalCalories = totalCalories,
            totalProtein = totalProtein,
            totalCarbs = totalCarbs,
            totalFat = totalFat,
            onClick = onDietClick
        )

        Spacer(Modifier.height(16.dp))

        // --- 【改动 2】恢复情况卡片 (包裹在淡紫色背景中) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = BgMain), // 使用淡紫色
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            // RecoveryDashboardView 内部自带 padding，这里不需要再加
            RecoveryDashboardView(regionStatus = bodyStatus, modifier = Modifier)
        }

        Spacer(Modifier.height(20.dp))
    }

    // 弹窗逻辑保持不变
    if (showActivityDialog) {
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
// ... (前面的代码保持不变)

// ==================== 子组件定义 ====================

// 【新增】独立的日期标题卡片
@Composable
fun DateHeaderCard(weekday: String, date: String, isToday: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // 【关键修改】将背景色强制修改为纯白色 (Color.White)
        // 原代码: colors = CardDefaults.cardColors(containerColor = if (isToday) BgCardTop else Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = weekday, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = date, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
            // 图标颜色也建议改成主题色，在白底上更清晰
            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

// ... (后面的 InfoItem, IconTitleHeader, StatusCard, DietCard 保持不变)

// InfoItem 组件保持不变...
@Composable
fun InfoItem(
    label: String,
    value: String,
    unit: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
        if (unit != null) {
            Spacer(Modifier.height(4.dp))
            Text(unit, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
        }
    }
}

// IconTitleHeader 组件保持不变...
@Composable
fun IconTitleHeader(
    icon: ImageVector,
    title: String,
    iconBgColor: Color,
    iconTintColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBgColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTintColor, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}


@Composable
fun StatusCard(
    activityData: com.benhe.fitlog.model.DailyActivity?,
    tdee: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = BgCardStatus),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // 【改动 3】标题改为中文 "状态"
            IconTitleHeader(
                icon = Icons.Default.Favorite,
                title = "状态",
                iconBgColor = AccentGreen.copy(alpha = 0.2f),
                iconTintColor = AccentGreen
            )
            Spacer(Modifier.height(20.dp))
            // 数据项行 (保持不变)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoItem(label = "睡眠", value = activityData?.sleepHours?.toString() ?: "--", unit = "HRS", modifier = Modifier.weight(1f))
                InfoItem(label = "强度", value = activityData?.intensity?.displayName ?: "未设置", modifier = Modifier.weight(1f))
                InfoItem(label = "消耗", value = tdee.toString(), unit = "KCAL", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DietCard(
    totalCalories: Double,
    totalProtein: Double,
    totalCarbs: Double,
    totalFat: Double,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = BgCardDiet),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // 标题栏
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                // 【改动 3】标题改为中文 "饮食"
                IconTitleHeader(
                    icon = Icons.Default.RestaurantMenu,
                    title = "饮食",
                    iconBgColor = AccentOrange.copy(alpha = 0.2f),
                    iconTintColor = AccentOrange
                )
                // 右侧总热量 (保持不变)
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextPrimary)) {
                            append(totalCalories.toInt().toString())
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextSecondary)) {
                            append(" KCAL")
                        }
                    }
                )
            }

            Spacer(Modifier.height(20.dp))
            // 数据项行 (保持不变)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoItem(label = "蛋白质", value = "${totalProtein.toInt()}g", modifier = Modifier.weight(1f))
                InfoItem(label = "碳水", value = "${totalCarbs.toInt()}g", modifier = Modifier.weight(1f))
                InfoItem(label = "脂肪", value = "${totalFat.toInt()}g", modifier = Modifier.weight(1f))
            }
        }
    }
}