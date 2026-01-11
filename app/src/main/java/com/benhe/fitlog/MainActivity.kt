package com.benhe.fitlog

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
                                date = selectedDate, viewModel = viewModel,
                                onAddClick = { /* 这里可以跳转到 DietScreen */ },
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
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 32.dp), pageSpacing = 16.dp) { page ->
            val date = dateList[page]
            // ✅ 修正：传入 date.toString() 和 DateUtils 返回的星期
            DayCard(
                date = date.toString(),
                weekday = DateUtils.getWeekday(date),
                isToday = date == LocalDate.now(),
                viewModel = viewModel,
                onDietClick = { onNavigateToDiet(date.toString()) }
            )
        }
    }
}

@Composable
fun DayCard(date: String, weekday: String, isToday: Boolean, viewModel: MainViewModel, onDietClick: () -> Unit) {
    // 实时观察数据
    val totalCalories by viewModel.getTotalCaloriesForDate(date).collectAsState(initial = 0.0)
    val totalProtein by viewModel.getTotalProteinForDate(date).collectAsState(initial = 0.0)

    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f),
        colors = CardDefaults.cardColors(containerColor = if (isToday) Color(0xFFE0E7FF) else Color(0xFFF3F4F6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            // ✅ 修复周周日：直接显示 weekday
            Text(text = weekday, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Spacer(Modifier.height(32.dp))

            // ✅ 使用 ExpandedModuleItem (大卡片)
            ExpandedModuleItem(
                title = "🍽 饮食",
                mainValue = if (totalCalories > 0) "${totalCalories.toInt()} kcal" else "点击记录",
                subItems = listOf("蛋白质" to "${totalProtein.toInt()}g", "碳水" to "0g", "脂肪" to "0g"),
                color = Color(0xFFFFF7ED),
                onClick = onDietClick
            )

            Spacer(Modifier.height(12.dp))

            ExpandedModuleItem(
                title = "🏋️ 训练",
                mainValue = "休息日",
                subItems = listOf("上次训练" to "2天前", "频率" to "3次", "状态" to "良好"),
                color = Color(0xFFEEF2FF),
                onClick = { /* 训练逻辑 */ }
            )
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
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(mainValue, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFFE67E22))
            }
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                subItems.forEach { (label, value) ->
                    Column(modifier = Modifier.padding(end = 24.dp)) {
                        Text(label, fontSize = 11.sp, color = Color.Gray)
                        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}