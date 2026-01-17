// 文件路径: com/benhe/fitlog/ui/theme/Color.kt
package com.benhe.fitlog.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==================== 旧主题的默认颜色 (修复报错用) ====================
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// ==================== 新设计的新颜色 ====================

// 品牌蓝色系 (对应网页的 brand-xxx)
val Brand50 = Color(0xFFf0f9ff)
val Brand100 = Color(0xFFe0f2fe)
val Brand400 = Color(0xFF38bdf8)
val Brand500 = Color(0xFF0ea5e9) // 主色调 Sky Blue
val Brand600 = Color(0xFF0284c7)

// 功能色
val MintGreen = Color(0xFFE6FFFA) // 薄荷绿背景
val Emerald400 = Color(0xFF34d399) // 翡翠绿
val Emerald500 = Color(0xFF10b981)
val Rose400 = Color(0xFFfb7185) // 玫瑰红 (用于日历数字)

// 文字颜色
val TextSlate800 = Color(0xFF1e293b) // 深色主标题
val TextSlate600 = Color(0xFF475569) // 次要文字
val TextSlate400 = Color(0xFF94a3b8) // 灰色小字/图标

// 核心：模拟毛玻璃效果的半透明白色背景
// 网页里的 glass class 是 background: rgba(255, 255, 255, 0.55)
val GlassWhite = Color.White.copy(alpha = 0.65f) // 稍微调高一点不透明度，在手机上效果更好
val GlassBorder = Color.White.copy(alpha = 0.4f)

// 背景渐变 (对应网页 body background linear-gradient)
val BackgroundGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFF0FFF4),
        Color(0xFFE6FFFA),
        Color(0xFFE0F2FE)
    )
)