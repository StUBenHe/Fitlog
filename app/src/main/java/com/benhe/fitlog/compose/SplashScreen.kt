package com.benhe.fitlog.compose



import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.R // 确保导入你的 R 文件以便引用图片资源
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit // 动画结束后的回调函数，用于跳转
) {
    // 1. 定义动画状态
    // Logo 的透明度和 Y 轴位移
    val logoAlpha = remember { Animatable(0f) }
    val logoOffsetY = remember { Animatable(100f) } // 初始状态向下偏移 100dp

    // 文字的透明度和 Y 轴位移
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(50f) } // 初始状态向下偏移 50dp

    // 2. 启动动画序列
    LaunchedEffect(key1 = true) {
        // 并行开始 Logo 的淡入和上移动画
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        launch {
            logoOffsetY.animateTo(
                targetValue = 0f, // 移动到最终位置 0
                animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
            )
        }

        // 稍微延迟 300ms 后，开始文字的动画，形成错落感
        delay(300)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        launch {
            textOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
            )
        }

        // 动画全部完成后，再等待 1.5 秒，然后调用回调进行页面跳转
        delay(2000)
        onSplashFinished()
    }

    // 3. UI 布局结构
    Box(modifier = Modifier.fillMaxSize()) {
        // --- 底层：背景图片 ---
        Image(
            painter = painterResource(id = R.drawable.splash_bg), // 引用背景图
            contentDescription = null,
            contentScale = ContentScale.Crop, // 铺满屏幕
            modifier = Modifier.fillMaxSize()
        )

        // --- 中间层：Logo 和文字 ---
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 动画 Logo
            Image(
                painter = painterResource(id = R.drawable.splash_logo), // 引用 Logo 图
                contentDescription = "Fitlog Logo",
                modifier = Modifier
                    .size(300.dp) // 设置 Logo 大小
                    .offset(y = logoOffsetY.value.dp) // 应用位移动画
                    .alpha(logoAlpha.value) // 应用透明度动画
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 动画主标题文字
            Text(
                text = "Fitlog",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A2342), // 深蓝色
                modifier = Modifier
                    .offset(y = textOffsetY.value.dp) // 应用位移动画
                    .alpha(textAlpha.value) // 应用透明度动画
            )

            // 动画副标题文字
            Text(
                text = "Your Health Journey",
                fontSize = 18.sp,
                color = Color(0xFF0A2342),
                modifier = Modifier
                    .offset(y = textOffsetY.value.dp)
                    .alpha(textAlpha.value)
            )
        }

        // --- 顶层：底部版权信息 ---
        Text(
            text = "© 2026 Ben He",
            fontSize = 14.sp,
            color = Color(0xFF0A2342),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(textAlpha.value) // 简单起见，让它跟随文字一起淡入
        )
    }
}