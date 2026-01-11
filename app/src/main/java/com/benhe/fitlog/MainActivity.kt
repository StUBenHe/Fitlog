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
import com.benhe.fitlog.ui.theme.ActivityInputDialog // 确保导入你写的弹窗
import com.benhe.fitlog.model.DailyActivity

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

                var currentScreen by remember { mutableIntStateOf(if (hasInit) 1 else 0) }
                var selectedDate by remember { mutableStateOf(LocalDate.now().toString()) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            0 -> ProfileScreen(onNavigateToCalendar = { currentScreen = 1 })
                            1 -> CalendarScreen(
                                viewModel = viewModel,
                                onNavigateToDiet = { date -> selectedDate = date; currentScreen = 2 },
                                onEditProfile = { currentScreen = 0 }
                            )
                            2 -> DailyDietListScreen(
                                date = selectedDate,
                                viewModel = viewModel,
                                onAddClick = { currentScreen = 3 },
                                onBack = { currentScreen = 1 }
                            )
                            3 -> DietScreen(
                                date = selectedDate,
                                viewModel = viewModel,
                                onBack = { currentScreen = 2 }
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
fun CalendarScreen(viewModel: MainViewModel, onNavigateToDiet: (String) -> Unit, onEditProfile: () -> Unit) {
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
                onDietClick = { onNavigateToDiet(dateString) }
            )
        }
    }
}

@Composable
fun DayCard(date: String, weekday: String, isToday: Boolean, viewModel: MainViewModel, onDietClick: () -> Unit) {
    // 1. 获取饮食数据 (略...)
    val totalCalories by viewModel.getTotalCaloriesForDate(date).collectAsState(initial = 0.0)
    val totalProtein by viewModel.getTotalProteinForDate(date).collectAsState(initial = 0.0)
    val totalCarbs by viewModel.getTotalCarbsForDate(date).collectAsState(initial = 0.0)
    val allRecords by viewModel.getDietRecordsForDate(date).collectAsState(initial = emptyList())
    val vitaminCount = allRecords.count { it.category == "维生素" }

    // 2. ✅ 获取状态数据 (必须先定义)
    val activityState = viewModel.getActivityForDate(date).collectAsState(initial = null)
    val activityData = activityState.value

    // 3. ✅ 计算 TDEE (必须放在 activityData 之后)
    val tdee = viewModel.getTodayExpenditure(activityData)

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
                // 如果开启了后燃效应，可以在 mainValue 加个小火苗 🔥
                mainValue = if (activityData != null) "${activityData!!.sleepHours}h" else "待记录",
                subItems = listOf(
                    "睡眠" to "${activityData?.sleepHours ?: "--"}h",
                    "强度" to (activityData?.intensity?.displayName ?: "未设置"),
                    "估计消耗" to "${tdee} kcal" // ✅ 这里使用了算好的 TDEE
                ),
                color = Color(0xFFF0FDF4),
                onClick = { showActivityDialog = true }
            )

            Spacer(Modifier.height(12.dp))

            // 3. 训练模块
            ExpandedModuleItem(
                title = "🏋️ 训练",
                mainValue = "休息日",
                subItems = listOf("上次训练" to "2天前", "频率" to "3次", "状态" to "良好"),
                color = Color(0xFFEEF2FF),
                onClick = { /* 训练逻辑 */ }
            )
        }
    }

    // 状态录入弹窗
    if (showActivityDialog) {
        ActivityInputDialog(
            initialSleep = activityData?.sleepHours ?: 8f,
            initialIntensity = activityData?.intensity ?: LifeIntensity.NORMAL,
            initialAfterburn = activityData?.isAfterburnEnabled ?: false, // ✅ 传入初始开关状态
            onDismiss = { showActivityDialog = false },
            onConfirm = { sleep, intensity, afterburn -> // ✅ 增加 afterburn 参数
                // ✅ 调用包含 4 个参数的更新方法
                viewModel.updateActivityForDate(date, sleep, intensity, afterburn)
                showActivityDialog = false
            }
        )
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


// 后燃开关
@Composable
fun ActivityInputDialog(
    initialSleep: Float,
    initialIntensity: LifeIntensity,
    initialAfterburn: Boolean, // ✅ 传入初始值
    onDismiss: () -> Unit,
    onConfirm: (Float, LifeIntensity, Boolean) -> Unit // ✅ 传出开关结果
) {
    var sleep by remember { mutableStateOf(initialSleep) }
    var intensity by remember { mutableStateOf(initialIntensity) }
    var afterburn by remember { mutableStateOf(initialAfterburn) } // ✅ 新增状态

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("记录今日状态") },
        text = {
            Column {
                Text("睡眠时间: ${String.format("%.1f", sleep)} 小时")
                Slider(value = sleep, onValueChange = { sleep = it }, valueRange = 4f..12f)

                Spacer(modifier = Modifier.height(16.dp))

                Text("生活强度:")
                // ... (强度选择代码不变) ...

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ 后燃效应开关 UI
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