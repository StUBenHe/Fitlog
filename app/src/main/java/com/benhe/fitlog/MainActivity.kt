package com.benhe.fitlog

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState // 虽然不再用于显隐逻辑，但保留它是个好习惯，万一以后需要高亮状态
import androidx.navigation.compose.rememberNavController
import com.benhe.fitlog.ui.History
import com.benhe.fitlog.ui.ProfileScreen
import com.benhe.fitlog.ui.RightProfileScreen
import com.benhe.fitlog.ui.WorkoutSessionScreen
import com.benhe.fitlog.ui.components.DailyDietListScreen
import com.benhe.fitlog.ui.components.GlassNavigationBar
import com.benhe.fitlog.ui.screens.DietScreen
import com.benhe.fitlog.ui.screens.HomeScreen
// ✅ 确保正确导入了 SplashScreen (假设它在 ui.screens 包下)
import com.benhe.fitlog.compose.SplashScreen
import com.benhe.fitlog.ui.theme.BackgroundGradient
import com.benhe.fitlog.ui.theme.FitlogTheme
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitlogTheme {
                // ✅ 创建顶层 NavController 来管理启动页和主内容之间的跳转
                val rootNavController = rememberNavController()
                val viewModel: MainViewModel = viewModel()

                // ✅ 使用顶层 NavHost，起始页设为 "splash"
                NavHost(navController = rootNavController, startDestination = "splash") {

                    // 1. 启动页路由
                    composable("splash") {
                        SplashScreen(
                            onSplashFinished = {
                                // 动画结束后跳转到主容器 "main_container"
                                // 并弹出 "splash"，防止按返回键回到启动页
                                rootNavController.navigate("main_container") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. 主应用容器路由
                    composable("main_container") {
                        // 这里包裹了你原来的主界面逻辑（背景和 MainContainerScreen）
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush = BackgroundGradient)
                        ) {
                            MainContainerScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

// ===================== 导航相关定义 (保持不变) =====================

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
    // 以下定义虽然没用到，但保留
    const val HOME_WITH_DATE = "home/{date}"
    fun homeWithDate(date: String) = "home/$date"
    const val DAILY_STATUS_DIALOG = "daily_status_dialog/{date}"
    fun dailyStatusDialog(date: String) = "daily_status_dialog/$date"
}

// ===================== 主界面容器 (修改点在这里) =====================
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContainerScreen(viewModel: MainViewModel) {
    // 这个 navController 是用于主界面内部底部导航和二级页面跳转的
    val bottomBarNavController = rememberNavController()
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
    val hasInit = remember { sharedPref.getBoolean("has_init", false) }
    val startRoute = if (hasInit) BottomNavItem.Home.route else BottomNavItem.Profile.route

    // 获取当前路由状态，虽然不用于显隐控制，但 GlassNavigationBar 内部可能需要它来决定哪个图标高亮
    // val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. 内容区域层
        NavHost(
            navController = bottomBarNavController,
            startDestination = startRoute,
            // 关键点：因为导航栏一直悬浮在底部，所以内容区域需要一直保留底部内边距，防止内容被遮挡
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            // --- 底部导航页面 ---
            composable(BottomNavItem.History.route) {
                History(
                    viewModel = viewModel,
                    onNavigateToDiet = { date -> bottomBarNavController.navigate(Routes.dietList(date)) },
                    onNavigateToWorkout = { date -> bottomBarNavController.navigate(Routes.workoutSession(date)) }
                )
            }

            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDietList = { date -> bottomBarNavController.navigate(Routes.dietList(date)) }
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
                    ProfileScreen(
                        onNavigateToCalendar = {
                            bottomBarNavController.navigate(BottomNavItem.Home.route) {
                                popUpTo(BottomNavItem.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }

            // --- 二级页面 ---
            composable(Routes.DIET_LIST) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
                DailyDietListScreen(
                    date = date,
                    viewModel = viewModel,
                    onAddClick = { bottomBarNavController.navigate(Routes.dietAdd(date)) },
                    onBack = { bottomBarNavController.popBackStack() }
                )
            }

            composable(Routes.DIET_ADD) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
                DietScreen(
                    date = date,
                    viewModel = viewModel,
                    onBack = { bottomBarNavController.popBackStack() }
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
        // ♻️ 修改内容：移除了之前的条件判断逻辑，现在无条件显示导航栏
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            GlassNavigationBar(navController = bottomBarNavController)
        }
    }
}