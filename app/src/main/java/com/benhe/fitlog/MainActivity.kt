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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.benhe.fitlog.ui.DietScreen
import com.benhe.fitlog.ui.LeftStatsScreen
import com.benhe.fitlog.ui.ProfileScreen
import com.benhe.fitlog.ui.RightProfileScreen
import com.benhe.fitlog.ui.components.DailyDietListScreen
import com.benhe.fitlog.ui.screens.HomeScreen
import com.benhe.fitlog.ui.screens.WorkoutSessionScreen
import com.benhe.fitlog.ui.theme.FitlogTheme
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitlogTheme {
                // ViewModel 依然在这里获取，传递给需要的屏幕
                val viewModel: MainViewModel = viewModel()
                MainContainerScreen(viewModel = viewModel)
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
}

// ===================== 主界面容器 (仅包含导航逻辑) =====================
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContainerScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }

    val bottomNavItems = listOf(
        BottomNavItem.History,
        BottomNavItem.Home,
        BottomNavItem.Workout,
        BottomNavItem.Profile
    )

    // 通用的导航回首页的函数（解决训练页返回崩溃问题）
    val navigateToHome = {
        navController.navigate(BottomNavItem.Home.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val isBottomNavRoute = bottomNavItems.any { it.route == currentDestination?.route }

            if (isBottomNavRoute) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = selected,
                            onClick = {
                                // 不再需要 if/else 判断，所有按钮统一处理
                                navController.navigate(item.route) {
                                    // 【核心修复】
                                    // 将弹出目标固定为 Home 路由。
                                    // 这意味着无论你点哪个 Tab，都会先回到 Home 这个基准点，
                                    // 保证了返回栈的稳定，解决了点击后应用消失的问题。
                                    popUpTo(BottomNavItem.Home.route) {
                                        saveState = true
                                    }
                                    // 保持单例模式，避免重复创建页面实例
                                    launchSingleTop = true
                                    // 恢复之前的状态（例如滚动位置）
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        val hasInit = remember { sharedPref.getBoolean("has_init", false) }
        val startRoute = if (hasInit) BottomNavItem.Home.route else BottomNavItem.Profile.route

        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- 底部导航页面 ---
            composable(BottomNavItem.History.route) {
                LeftStatsScreen(
                    viewModel = viewModel,
                    onNavigateToDiet = { date -> navController.navigate(Routes.dietList(date)) },
                    onNavigateToWorkout = { date -> navController.navigate(Routes.workoutSession(date)) }
                )
            }

            composable(BottomNavItem.Home.route) {
                // 这里引用了移到 ui/screens/ 下的 HomeScreen
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDietList = { date -> navController.navigate(Routes.dietList(date)) }
                )
            }

            composable(BottomNavItem.Workout.route) {
                // 获取今天的日期，用于回显
                val today = LocalDate.now().toString()
                // 显示锻炼记录主界面
                WorkoutSessionScreen(
                    date = today,
                    viewModel = viewModel
                    // 注意：这里不需要 onBack 参数了，因为这是一级页面
                )
            }
            composable(BottomNavItem.Profile.route) {
                // 修复：根据初始化状态显示不同页面
                val currentHasInit = sharedPref.getBoolean("has_init", false)
                if (currentHasInit) {
                    RightProfileScreen(viewModel = viewModel)
                } else {
                    ProfileScreen(
                        onNavigateToCalendar = { navigateToHome() }
                    )
                }
            }

            // --- 二级页面 ---
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
                    viewModel = viewModel,

                )
            }
        }
    }
}