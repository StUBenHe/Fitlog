package com.benhe.fitlog.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RightProfileScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    // 1. 获取 SharedPreference (用于存取基本死数据：名字、身高、年龄)
    val prefs = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }

    // 2. 获取数据库 Flow (用于存取动态变化数据：体重、体脂)
    val latestStat by viewModel.latestBodyStat.collectAsState()

    // --- 数据读取逻辑 ---
    // 基本信息从 SP 读
    var name by remember { mutableStateOf(prefs.getString("name", "朋友") ?: "朋友") }
    var age by remember { mutableStateOf(prefs.getString("age", "25") ?: "25") }
    var height by remember { mutableStateOf(prefs.getString("height", "175") ?: "175") }

    // 体重信息优先从数据库读(latestStat)，如果没有，再尝试从 SP 读旧数据作为兜底
    val dbWeight = latestStat?.weight
    val dbFat = latestStat?.bodyFatRate

    val displayWeight = dbWeight?.toString() ?: prefs.getString("weight", "--") ?: "--"
    val displayFat = dbFat?.toString() ?: prefs.getString("bodyFat", "--") ?: "--"

    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("个人档案", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = { showEditDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "编辑")
            }
        }

        Spacer(Modifier.height(24.dp))

        // 信息展示卡片
        Card(elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileRow("昵称", name)
                Divider(Modifier.padding(vertical = 12.dp))
                ProfileRow("年龄", "$age 岁")
                Divider(Modifier.padding(vertical = 12.dp))
                ProfileRow("身高", "$height cm")
                Divider(Modifier.padding(vertical = 12.dp))
                // 这些数据现在关联了数据库
                ProfileRow("当前体重", "$displayWeight kg")
                Divider(Modifier.padding(vertical = 12.dp))
                ProfileRow("体脂率", "$displayFat %")
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = "提示：更新体重数据将自动保存至历史记录，生成左侧趋势图。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }

    // 编辑弹窗
    if (showEditDialog) {
        EditFullProfileDialog(
            initialName = name,
            initialAge = age,
            initialHeight = height,
            initialWeight = if (displayWeight == "--") "" else displayWeight,
            initialFat = if (displayFat == "--") "" else displayFat,
            onDismiss = { showEditDialog = false },
            onConfirm = { nName, nAge, nHeight, nWeight, nFat ->
                // 1. 更新 UI
                name = nName
                age = nAge
                height = nHeight

                // 2. 基本信息存入 SP
                with(prefs.edit()) {
                    putString("name", nName)
                    putString("age", nAge)
                    putString("height", nHeight)
                    // 同时把体重存一份 SP 做备份
                    putString("weight", nWeight)
                    putString("bodyFat", nFat)
                    apply()
                }

                // 3. 核心：体重数据存入 数据库 (为了画图)
                val w = nWeight.toFloatOrNull() ?: 0f
                val f = nFat.toFloatOrNull() ?: 0f
                if (w > 0) {
                    viewModel.saveBodyStat(w, f)
                }

                showEditDialog = false
            }
        )
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

// 包含所有字段的完整编辑弹窗
@Composable
fun EditFullProfileDialog(
    initialName: String,
    initialAge: String,
    initialHeight: String,
    initialWeight: String,
    initialFat: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var age by remember { mutableStateOf(initialAge) }
    var height by remember { mutableStateOf(initialHeight) }
    var weight by remember { mutableStateOf(initialWeight) }
    var fat by remember { mutableStateOf(initialFat) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("编辑个人资料") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("昵称") })
                OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("年龄") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("身高 (cm)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Divider()
                Text("身体数据 (用于统计)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("体重 (kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("体脂率 (%)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name, age, height, weight, fat) }) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}