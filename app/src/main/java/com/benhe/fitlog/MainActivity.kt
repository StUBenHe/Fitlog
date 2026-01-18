package com.benhe.fitlog

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.benhe.fitlog.ui.screens.DietScreen
import com.benhe.fitlog.ui.History
import com.benhe.fitlog.ui.ProfileScreen
import com.benhe.fitlog.ui.RightProfileScreen
import com.benhe.fitlog.ui.components.DailyDietListScreen
import com.benhe.fitlog.ui.screens.HomeScreen
import com.benhe.fitlog.ui.WorkoutSessionScreen
import com.benhe.fitlog.ui.theme.FitlogTheme
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.benhe.fitlog.ui.theme.BackgroundGradient // 导入刚才定义的渐变
import androidx.compose.ui.Alignment

import com.benhe.fitlog.ui.theme.*
import androidx.compose.ui.unit.dp
// ✅ 核心修復：確保導入了 GlassNavigationBar
import com.benhe.fitlog.ui.components.GlassNavigationBar



class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitlogTheme {
                // ViewModel 依然在这里获取，传递给需要的屏幕
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = BackgroundGradient) // 使用新背景
                ) {
                    val viewModel: MainViewModel = viewModel()
                    // 3. 加载主容器
                    MainContainerScreen(viewModel = viewModel)

                }
            }
        }
    }
}

// ===================== 导航相关定义 =====================

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object History : BottomNavItem("history", "历史日历", Icons.Default.DateRange)
    object Home : BottomNavItem("home", "当日状态", Icons.Default.Home)
    object Workout : BottomNavItem("workout", "锻炼项目", Icons.Default.FitnessCenter)
    object Profile : BottomNavItem("profile", "个人", Icons.Default.Person)
}

object Routes {
    const val DIET_LIST = "diet_list/{date}"
    const val DIET_ADD = "diet_add/{date}"
    const val WORKOUT_SESSION = "workout_session/{date}"

    fun dietList(date: String) = "diet_list/$date"
    fun dietAdd(date: String) = "diet_add/$date"
    fun workoutSession(date: String) = "workout_session/$date"
    const val HOME_WITH_DATE = "home/{date}"
    fun homeWithDate(date: String) = "home/$date"
    const val DAILY_STATUS_DIALOG = "daily_status_dialog/{date}"
    fun dailyStatusDialog(date: String) = "daily_status_dialog/$date"
}

// ===================== 主界面容器 (仅包含导航逻辑) =====================
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContainerScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
    val hasInit = remember { sharedPref.getBoolean("has_init", false) }
    val startRoute = if (hasInit) BottomNavItem.Home.route else BottomNavItem.Profile.route

    // ✅ 放弃 Scaffold，使用 Box 进行堆叠布局
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. 内容区域层
        NavHost(
            navController = navController,
            startDestination = startRoute,
            // ✅ 【修复核心】移除了 innerPadding，改为手动设置底部 padding，为悬浮导航栏留位置
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            // --- 底部导航页面 ---
            composable(BottomNavItem.History.route) {
                History(
                    viewModel = viewModel,
                    onNavigateToDiet = { date -> navController.navigate(Routes.dietList(date)) },
                    onNavigateToWorkout = { date -> navController.navigate(Routes.workoutSession(date)) }
                )
            }

            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDietList = { date -> navController.navigate(Routes.dietList(date)) }
                )
            }

            composable(BottomNavItem.Workout.route) {
                val today = LocalDate.now().toString()
                WorkoutSessionScreen(
                    date = today,
                    viewModel = viewModel
                )
            }

            composable(BottomNavItem.Profile.route) {
                val currentHasInit = sharedPref.getBoolean("has_init", false)
                if (currentHasInit) {
                    RightProfileScreen(viewModel = viewModel)
                } else {
                    // ✅ 【修复核心】移除了错误的 navigateToHome 调用
                    ProfileScreen(
                        onNavigateToCalendar = {
                            // 直接在这里写导航逻辑
                            navController.navigate(BottomNavItem.Home.route) {
                                popUpTo(BottomNavItem.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }

            // --- 二级页面 (保持不变) ---
            composable(Routes.DIET_LIST) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
                DailyDietListScreen(
                    date = date,
                    viewModel = viewModel,
                    onAddClick = { navController.navigate(Routes.dietAdd(date)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.DIET_ADD) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
                DietScreen(
                    date = date,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.WORKOUT_SESSION) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
                WorkoutSessionScreen(
                    date = date,
                    viewModel = viewModel
                )
            }
        }

        // 2. 导航栏层 (悬浮在最上方)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val showBottomBar = currentRoute in listOf(
            BottomNavItem.History.route,
            BottomNavItem.Home.route,
            BottomNavItem.Workout.route,
            BottomNavItem.Profile.route
        )

        if (showBottomBar) {
            // 将导航栏对齐到底部中心
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                GlassNavigationBar(navController = navController)
            }
        }
    }
}