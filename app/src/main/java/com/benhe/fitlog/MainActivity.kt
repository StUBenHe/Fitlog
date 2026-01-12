package com.benhe.fitlog

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benhe.fitlog.logic.DateUtils
import com.benhe.fitlog.model.BodyRegion
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.ui.DietScreen
import com.benhe.fitlog.ui.ProfileScreen
import com.benhe.fitlog.ui.screens.DailyDietListScreen
import com.benhe.fitlog.ui.theme.FitlogTheme
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate

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
                                onNavigateToWorkout = { date -> selectedDate = date; currentScreen = 4 },
                                onEditProfile = { currentScreen = 0 }
                            )
                            2 -> DailyDietListScreen(
                                date = selectedDate, viewModel = viewModel,
                                onAddClick = { currentScreen = 3 }, onBack = { currentScreen = 1 }
                            )
                            3 -> DietScreen(
                                date = selectedDate, viewModel = viewModel, onBack = { currentScreen = 2 }
                            )
                            4 -> WorkoutSessionScreen(
                                date = selectedDate, viewModel = viewModel, onBack = { currentScreen = 1 }
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
    onNavigateToWorkout: (String) -> Unit,
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
                onWorkoutClick = { onNavigateToWorkout(dateString) }
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
    onWorkoutClick: () -> Unit
) {
    val totalCalories by viewModel.getTotalCaloriesForDate(date).collectAsState(initial = 0.0)
    val totalProtein by viewModel.getTotalProteinForDate(date).collectAsState(initial = 0.0)
    val totalCarbs by viewModel.getTotalCarbsForDate(date).collectAsState(initial = 0.0)
    val allRecords by viewModel.getDietRecordsForDate(date).collectAsState(initial = emptyList())
    val vitaminCount = allRecords.count { it.category == "维生素" }

    val activityState = viewModel.getActivityForDate(date).collectAsState(initial = null)
    val activityData = activityState.value
    val tdee = viewModel.getTodayExpenditure(activityData)

    val bodyStatus by viewModel.bodyStatus.collectAsState()
    // 恢复逻辑：状态低于 0.99 表示疲劳
    val activeLoads = bodyStatus.entries
        .filter { it.value < 0.99f }
        .sortedBy { it.value }

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

            ExpandedModuleItem(
                title = "🏋️ 训练",
                mainValue = if (activeLoads.isEmpty()) "状态极佳" else "恢复中",
                subItems = if (activeLoads.isEmpty()) {
                    listOf("建议" to "可冲击重量", "状态" to "100%", "提示" to "开始训练")
                } else {
                    activeLoads.take(3).map { (region, status) ->
                        region.displayName to "${(status * 100).toInt()}%"
                    }
                },
                color = Color(0xFFEEF2FF),
                onClick = onWorkoutClick
            )

            Spacer(Modifier.height(16.dp))

            // 🏆 身体状态看板
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text("🏆 身体状态", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                RecoveryDashboardView(regionStatus = bodyStatus)
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
fun RecoveryDashboardView(regionStatus: Map<BodyRegion, Float>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        regionStatus.forEach { (region, status) ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(region.displayName, modifier = Modifier.width(50.dp), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                Box(modifier = Modifier.weight(1f).height(8.dp).background(Color(0xFFE0E0E0), CircleShape)) {
                    Box(modifier = Modifier.fillMaxWidth(status).fillMaxHeight().background(
                        when {
                            status < 0.4f -> Color(0xFFFF5252)
                            status < 0.8f -> Color(0xFF4A90E2)
                            else -> Color(0xFF4CAF50)
                        }, CircleShape
                    ))
                }
                Text("${(status * 100).toInt()}%", modifier = Modifier.padding(start = 8.dp).width(35.dp), style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
fun WorkoutSessionScreen(date: String, viewModel: MainViewModel, onBack: () -> Unit) {
    val todaySets by viewModel.getSetsByDate(date).collectAsState(initial = emptyList())
    val draftState = remember { mutableStateMapOf<BodyRegion, Pair<Int, String>>() }

    LaunchedEffect(todaySets) {
        BodyRegion.entries.forEach { draftState[it] = Pair(0, "") }
        todaySets.forEach { set -> draftState[set.region] = Pair(set.rpe ?: 0, set.note ?: "") }
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("$date 训练记录") }, navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "返回") }
            })
        },
        bottomBar = {
            Button(
                onClick = { viewModel.syncWorkoutSets(date, draftState.toMap()); onBack() },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("保存今日训练记录") }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(BodyRegion.entries) { region ->
                val state = draftState[region] ?: Pair(0, "")
                WorkoutRegionCard(region.displayName, state.first, state.second) { s, n -> draftState[region] = Pair(s, n) }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun WorkoutRegionCard(name: String, stars: Int, note: String, onUpdate: (Int, String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row {
                    repeat(5) { i ->
                        val s = i + 1
                        Text(
                            if (s <= stars) "★" else "☆",
                            fontSize = 24.sp,
                            color = if (s <= stars) Color(0xFFE67E22) else Color(0xFFD1D5DB),
                            modifier = Modifier.clickable { onUpdate(if (stars == s) 0 else s, note) }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = note, onValueChange = { onUpdate(stars, it) },
                placeholder = { Text("录入动作详情...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityInputDialog(
    initialSleep: Float,
    initialIntensity: LifeIntensity,
    initialAfterburn: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Float, LifeIntensity, Boolean) -> Unit
) {
    var sleep by remember { mutableFloatStateOf(initialSleep) }
    var intensity by remember { mutableStateOf(initialIntensity) }
    var afterburn by remember { mutableStateOf(initialAfterburn) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📝 记录今日状态", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 1. 睡眠时间
                Text("睡眠时间: ${String.format("%.1f", sleep)} 小时", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = sleep,
                    onValueChange = { sleep = it },
                    valueRange = 4f..12f,
                    steps = 15
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. 生活强度选择按钮 (这就是你找的按钮逻辑)
                Text("生活强度: ${intensity.displayName}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    LifeIntensity.entries.forEach { item ->
                        val isSelected = intensity == item
                        FilterChip(
                            selected = isSelected,
                            onClick = { intensity = item },
                            label = { Text(item.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. 后燃效应
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("🔥 后燃效应", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "开启后代谢额外提升10%",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = afterburn,
                        onCheckedChange = { afterburn = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(sleep, intensity, afterburn) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}