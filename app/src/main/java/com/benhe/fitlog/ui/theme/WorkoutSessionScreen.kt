package com.benhe.fitlog.ui.theme


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // ✅ 修复 Color 报错
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    date: String,
    viewModel: MainViewModel, // 暂时未用到，警告是正常的，后续录入数据会用到
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$date 训练记录") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    /* 下一步逻辑：弹出动作库选择器 */
                },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("添加动作") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("今日暂无训练记录", color = Color.Gray)
                Text("点击右下角开始流汗 💦", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}