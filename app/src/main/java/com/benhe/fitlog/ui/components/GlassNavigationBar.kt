// 文件路径: com/benhe/fitlog/ui/components/GlassNavigationBar.kt
package com.benhe.fitlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
// 确保这个包名和你的项目一致
import com.benhe.fitlog.BottomNavItem
import com.benhe.fitlog.ui.theme.*

@Composable
fun GlassNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val bottomNavItems = listOf(
        BottomNavItem.History,
        BottomNavItem.Home,
        BottomNavItem.Workout,
        BottomNavItem.Profile
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(32.dp), spotColor = Color.Black.copy(alpha = 0.1f))
            .clip(RoundedCornerShape(32.dp))
            .background(GlassWhite)
            .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                GlassNavItem(
                    item = item,
                    isSelected = selected
                ) {
                    // ================== 修改区域开始 ==================
                    navController.navigate(item.route) {
                        // 1. 弹出到 Home 路由，避免返回栈无限增长
                        popUpTo(BottomNavItem.Home.route) {
                            // ✅ 关键修改：设置为 false (或直接删除这一行，默认为 false)
                            // 作用：离开当前 Tab 时，不保存其状态
                            saveState = false
                        }
                        // 2. 避免同一个页面在栈顶被重复创建
                        launchSingleTop = true

                        // ✅ 关键修改：设置为 false
                        // 作用：点击 Tab 时，不恢复之前的状态，强制显示该 Tab 的起始页面
                        restoreState = false
                    }
                    // ================== 修改区域结束 ==================
                }
            }
        }
    }
}

@Composable
fun GlassNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor = if (isSelected) Brand500 else TextSlate400
    val labelColor = if (isSelected) TextSlate800 else TextSlate400
    val scaleFactor = if (isSelected) 1.1f else 1.0f

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
            .scale(scaleFactor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = iconColor,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = labelColor,
            letterSpacing = 0.5.sp
        )
    }
}