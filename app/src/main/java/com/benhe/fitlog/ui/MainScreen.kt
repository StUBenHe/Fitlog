package com.benhe.fitlog.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.benhe.fitlog.CalendarScreen
import com.benhe.fitlog.viewmodel.MainViewModel

// 定义底部导航的三个选项
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Stats : BottomNavItem("stats", Icons.Default.DateRange, "统计")
    object Home : BottomNavItem("home", Icons.Default.Home, "主页")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "状态")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToDiet: (String) -> Unit,
    onNavigateToWorkout: (String) -> Unit,
    onEditProfile: () -> Unit
) {
    // 默认选中中间的 Home (你原来的 CalendarScreen)
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(BottomNavItem.Stats, BottomNavItem.Home, BottomNavItem.Profile)
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedTab == item,
                        onClick = { selectedTab = item }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 使用 Box 容纳内容，根据 selectedTab 切换显示不同的 Screen
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                // 1. 左侧模块：统计图表 + 凯尔特结 (我们之前写的)
                BottomNavItem.Stats -> {
                    LeftStatsScreen(
                        viewModel = viewModel,
                        onNavigateToDiet = onNavigateToDiet,
                        onNavigateToWorkout = onNavigateToWorkout
                    )
                }

                // 2. 中间模块：你原来的日历主页 (保持不变)
                BottomNavItem.Home -> {
                    CalendarScreen(
                        viewModel = viewModel,
                        onNavigateToDiet = onNavigateToDiet,
                        onNavigateToWorkout = onNavigateToWorkout,
                        onEditProfile = {
                            // 点击主页名字时，自动跳转到右侧“个人状态”页，而不是原来的弹窗
                            selectedTab = BottomNavItem.Profile
                        }
                    )
                }

                // 3. 右侧模块：个人数据 + 修改 (我们之前写的)
                BottomNavItem.Profile -> {
                    RightProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}

