package com.benhe.fitlog.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

import androidx.compose.ui.graphics.vector.ImageVector


// 定义底部导航的三个选项
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Stats : BottomNavItem("stats", Icons.Default.DateRange, "统计")
    object Home : BottomNavItem("home", Icons.Default.Home, "主页")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "状态")
}

