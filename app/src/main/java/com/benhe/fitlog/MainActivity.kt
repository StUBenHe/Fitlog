package com.benhe.fitlog

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benhe.fitlog.logic.DateUtils
import com.benhe.fitlog.ui.ProfileScreen
import com.benhe.fitlog.ui.screens.DailyDietListScreen
import com.benhe.fitlog.ui.theme.FitlogTheme
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate
import com.benhe.fitlog.ui.DietScreen
import com.benhe.fitlog.data.db.DietRecord
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.ui.theme.ActivityInputDialog
import com.benhe.fitlog.model.DailyActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitlogTheme {
                val viewModel: MainViewModel = viewModel()
                val context = LocalContext.current
                val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
                val hasInit = remember { sharedPref.getBoolean("has_init", false) }

                // 控制路由的状态
                var currentScreen by remember { mutableIntStateOf(if (hasInit) 1 else 0) }
                var selectedDate by remember { mutableStateOf(LocalDate.now().toString()) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            0 -> ProfileScreen(onNavigateToCalendar = { currentScreen = 1 })

                            // 1. 主日历页
                            1 -> CalendarScreen(
                                viewModel = viewModel,
                                onNavigateToDiet = { date -> selectedDate = date; currentScreen = 2 },
                                // ✅ 重点：这里处理训练卡片的跳转请求
                                onNavigateToWorkout = { date -> selectedDate = date; currentScreen = 4 },
                                onEditProfile = { currentScreen = 0 }
                            )

                            // 2. 饮食列表页
                            2 -> DailyDietListScreen(
                                date = selectedDate,
                                viewModel = viewModel,
                                onAddClick = { currentScreen = 3 },
                                onBack = { currentScreen = 1 }
                            )

                            // 3. 饮食添加页
                            3 -> DietScreen(
                                date = selectedDate,
                                viewModel = viewModel,
                                onBack = { currentScreen = 2 }
                            )

                            // 4. ✅ 训练详情页 (新增加的界面)
                            4 -> WorkoutSessionScreen(
                                date = selectedDate,
                                viewModel = viewModel,
                                onBack = { currentScreen = 1 }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen(
    viewModel: MainViewModel,
    onNavigateToDiet: (String) -> Unit,
    onNavigateToWorkout: (String) -> Unit, // ✅ 增加参数
    onEditProfile: () -> Unit
) {
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
    val name = sharedPref.getString("username", "朋友") ?: "朋友"

    val dateList = remember { DateUtils.getCalendarRange() }
    val pagerState = rememberPagerState(initialPage = 15) { dateList.size }

    Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
        Text(
            text = "👋 你好，$name!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 24.dp).clickable { onEditProfile() },
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp
        ) { page ->
            val dateString = dateList[page].toString()
            DayCard(
                date = dateString,
                weekday = DateUtils.getWeekday(dateList[page]),
                isToday = dateList[page] == LocalDate.now(),
                viewModel = viewModel,
                onDietClick = { onNavigateToDiet(dateString) },
                onWorkoutClick = { onNavigateToWorkout(dateString) } // ✅ 传入回调
            )
        }
    }
}

@Composable
fun DayCard(
    date: String,
    weekday: String,
    isToday: Boolean,
    viewModel: MainViewModel,
    onDietClick: () -> Unit,
    onWorkoutClick: () -> Unit // ✅ 增加参数
) {
    // 数据收集部分
    val totalCalories by viewModel.getTotalCaloriesForDate(date).collectAsState(initial = 0.0)
    val totalProtein by viewModel.getTotalProteinForDate(date).collectAsState(initial = 0.0)
    val totalCarbs by viewModel.getTotalCarbsForDate(date).collectAsState(initial = 0.0)
    val allRecords by viewModel.getDietRecordsForDate(date).collectAsState(initial = emptyList())
    val vitaminCount = allRecords.count { it.category == "维生素" }

    val activityState = viewModel.getActivityForDate(date).collectAsState(initial = null)
    val activityData = activityState.value
    val tdee = viewModel.getTodayExpenditure(activityData)

    // 身体状态数据
    val bodyStatus by viewModel.bodyStatus.collectAsState()
    val activeLoads = bodyStatus.entries
        .filter { it.value > 0.1f }
        .sortedByDescending { it.value }
        .take(3)
        .map { it.key to it.value }

    var showActivityDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.95f),
        colors = CardDefaults.cardColors(containerColor = if (isToday) Color(0xFFE0E7FF) else Color(0xFFF3F4F6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = weekday, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Spacer(Modifier.height(24.dp))

            // 1. 饮食模块
            ExpandedModuleItem(
                title = "🍽 饮食",
                mainValue = if (totalCalories > 0) "${totalCalories.toInt()} kcal" else "点击记录",
                subItems = listOf(
                    "蛋白质" to "${totalProtein.toInt()}g",
                    "碳水" to "${totalCarbs.toInt()}g",
                    "维生素" to "${vitaminCount} 种"
                ),
                color = Color(0xFFFFF7ED),
                onClick = onDietClick
            )

            Spacer(Modifier.height(12.dp))

            // 2. 每日状态模块
            ExpandedModuleItem(
                title = "🏃 状态",
                mainValue = if (activityData != null) "${activityData.sleepHours}h" else "待记录",
                subItems = listOf(
                    "睡眠" to "${activityData?.sleepHours ?: "--"}h",
                    "强度" to (activityData?.intensity?.displayName ?: "未设置"),
                    "估计消耗" to "${tdee} kcal"
                ),
                color = Color(0xFFF0FDF4),
                onClick = { showActivityDialog = true }
            )

            Spacer(Modifier.height(12.dp))

            // 3. 🏋️ 训练模块
            ExpandedModuleItem(
                title = "🏋️ 训练",
                mainValue = if (activeLoads.isEmpty()) "状态极佳" else "恢复中",
                subItems = if (activeLoads.isEmpty()) {
                    listOf("建议" to "可冲击重量", "状态" to "100%", "提示" to "开始训练")
                } else {
                    activeLoads.map { (region, load) ->
                        region.displayName to "${(load * 100).toInt()}%"
                    }
                },
                color = Color(0xFFEEF2FF),
                onClick = onWorkoutClick // ✅ 这里现在会触发跳转
            )

            if (isToday && activeLoads.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                BodyLoadQuickView(activeLoads)
            }
        }
    }

    if (showActivityDialog) {
        ActivityInputDialog(
            initialSleep = activityData?.sleepHours ?: 8f,
            initialIntensity = activityData?.intensity ?: LifeIntensity.NORMAL,
            initialAfterburn = activityData?.isAfterburnEnabled ?: false,
            onDismiss = { showActivityDialog = false },
            onConfirm = { sleep, intensity, afterburn ->
                viewModel.updateActivityForDate(date, sleep, intensity, afterburn)
                showActivityDialog = false
            }
        )
    }
}

/**
 * 新增加的训练详情页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(date: String, viewModel: MainViewModel, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$date 训练记录") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* 下一步：开发动作选择器 */ },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("添加动作") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("今日暂无训练记录，请点击右下角添加", color = Color.Gray)
        }
    }
}

@Composable
fun ExpandedModuleItem(title: String, mainValue: String, subItems: List<Pair<String, String>>, color: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(mainValue, fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color(0xFFE67E22))
            }
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                subItems.forEach { (label, value) ->
                    Column(modifier = Modifier.padding(end = 20.dp)) {
                        Text(label, fontSize = 10.sp, color = Color.Gray)
                        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityInputDialog(
    initialSleep: Float,
    initialIntensity: LifeIntensity,
    initialAfterburn: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Float, LifeIntensity, Boolean) -> Unit
) {
    var sleep by remember { mutableStateOf(initialSleep) }
    var intensity by remember { mutableStateOf(initialIntensity) }
    var afterburn by remember { mutableStateOf(initialAfterburn) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("记录今日状态") },
        text = {
            Column {
                Text("睡眠时间: ${String.format("%.1f", sleep)} 小时")
                Slider(value = sleep, onValueChange = { sleep = it }, valueRange = 4f..12f)
                Spacer(modifier = Modifier.height(16.dp))
                Text("生活强度: ${intensity.displayName}")
                // 这里可以根据需要补全强度选择按钮...
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("后燃效应", style = MaterialTheme.typography.bodyLarge)
                        Text("开启后代谢额外提升10%", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(checked = afterburn, onCheckedChange = { afterburn = it })
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(sleep, intensity, afterburn) }) { Text("确定") }
        }
    )
}

@Composable
fun BodyLoadQuickView(loads: List<Pair<com.benhe.fitlog.model.BodyRegion, Float>>) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        loads.forEach { (region, load) ->
            val color = when {
                load < 0.4f -> Color(0xFF4CAF50)
                load < 0.8f -> Color(0xFFFFC107)
                else -> Color(0xFFF44336)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(region.displayName, fontSize = 10.sp, modifier = Modifier.width(50.dp))
                LinearProgressIndicator(
                    progress = load.coerceAtMost(1f),
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = color,
                    trackColor = color.copy(alpha = 0.2f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}