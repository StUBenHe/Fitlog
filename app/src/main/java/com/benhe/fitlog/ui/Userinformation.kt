package com.benhe.fitlog.ui

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.benhe.fitlog.viewmodel.MainViewModel

// 定义颜色
val CyanBg = Color(0xFFE0F7FA)
val CardCyanBg = Color(0xFFEFFDFD)
val CyanAccent = Color(0xFF00BCD4)
val TextGray = Color(0xFF757575)
val TextDark = Color(0xFF37474F)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RightProfileScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
    val latestStat by viewModel.latestBodyStat.collectAsState()

    // --- 1. 数据读取与状态定义 ---
    // 基本信息 (从 SP 读取，弹窗编辑时更新)
    var name by remember { mutableStateOf(prefs.getString("name", "Zenith User") ?: "Zenith User") }
    var age by remember { mutableStateOf(prefs.getString("age", "25") ?: "25") }
    var height by remember { mutableStateOf(prefs.getString("height", "170") ?: "170") }
    var gender by remember { mutableStateOf(prefs.getString("gender", "male") ?: "male") }

    // 数据库中的最新数据 (用于初始化输入框)
    val dbWeightStr = latestStat?.weight?.toString()
    val dbFatStr = latestStat?.bodyFatRate?.toString()
    // SP 中的备份数据 (兜底)
    val spWeightStr = prefs.getString("weight", null)
    val spFatStr = prefs.getString("bodyFat", null)

    // 最终决定用于显示的初始值
    val initialDisplayWeight = dbWeightStr ?: spWeightStr ?: ""
    val initialDisplayFat = dbFatStr ?: spFatStr ?: ""

    // 【重要变动】卡片上直接输入的临时状态
    // 使用 remember(key) 确保当数据库数据更新时，输入框初始值也能跟上(如果用户没在输入的话)
    var inputWeight by remember(initialDisplayWeight) { mutableStateOf(initialDisplayWeight) }
    var inputFat by remember(initialDisplayFat) { mutableStateOf(initialDisplayFat) }

    var showEditDialog by remember { mutableStateOf(false) }

    // --- 2. 保存逻辑 ---

    // 逻辑A: 仅保存卡片上的测量数据 (点击卡片按钮触发)
    val onSaveMeasurementsOnly: () -> Unit = {
        // 1. 保存备份到 SP (使用 KTX edit {} 语法)
        prefs.edit {
            if (inputWeight.isNotEmpty()) putString("weight", inputWeight)
            if (inputFat.isNotEmpty()) putString("bodyFat", inputFat)
        }

        // 2. 保存到数据库
        val w = inputWeight.toFloatOrNull() ?: 0f
        val f = inputFat.toFloatOrNull() ?: 0f
        if (w > 0 || f > 0) {
            viewModel.saveBodyStat(w, f)
            Toast.makeText(context, "今日数据已保存", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "请输入有效数值", Toast.LENGTH_SHORT).show()
        }
    }

    // 逻辑B: 保存完整档案 (点击弹窗保存触发)
    val onSaveFullProfile: (String, String, String, String, String, String) -> Unit = { nName, nAge, nHeight, nGender, nWeight, nFat ->
        name = nName
        age = nAge
        height = nHeight
        gender = nGender

        prefs.edit {
            putString("name", nName)
            putString("age", nAge)
            putString("height", nHeight)
            putString("gender", nGender)
            if (nWeight.isNotEmpty()) putString("weight", nWeight)
            if (nFat.isNotEmpty()) putString("bodyFat", nFat)
        }

        val w = nWeight.toFloatOrNull() ?: 0f
        val f = nFat.toFloatOrNull() ?: 0f
        if (w > 0 || f > 0) {
            viewModel.saveBodyStat(w, f)
        }
        // 更新卡片输入框的显示
        inputWeight = nWeight
        inputFat = nFat
        showEditDialog = false
    }


    // --- 3. UI 布局 ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyanBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // --- 顶部头像区域 ---
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { showEditDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (gender == "female") Icons.Default.Female else Icons.Default.Male,
                        contentDescription = "Gender Identity",
                        modifier = Modifier.size(60.dp),
                        tint = CyanAccent
                    )
                }
                // 【改动】图标换回 Edit (笔)
                Surface(
                    shape = CircleShape,
                    color = CyanAccent,
                    modifier = Modifier.size(32.dp).offset(x = 4.dp, y = 4.dp),
                    shadowElevation = 4.dp
                ) {
                    IconButton(onClick = { showEditDialog = true }, modifier = Modifier.padding(4.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("个人档案", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextDark)
            Text("MY FITNESS IDENTITY", style = MaterialTheme.typography.labelMedium, color = CyanAccent, letterSpacing = 2.sp)

            Spacer(Modifier.height(32.dp))

            // --- 基本信息卡片 ---
            StyledCard {
                Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)) {
                    StyledProfileRow("昵称", name)
                    HorizontalDivider(color = CyanAccent.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))
                    StyledProfileRow("年龄", "$age 岁")
                    HorizontalDivider(color = CyanAccent.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))
                    StyledProfileRow("身高", "$height cm")
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- 【改动重点】今日身体数据卡片 (改为直接输入) ---
            StyledCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("今日身体数据", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark)
                        }
                        Surface(
                            color = CyanAccent.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("每日更新", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // 【改动】输入框区域
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // 体重输入框
                        CardInputItem(
                            label = "体重 (KG)",
                            value = inputWeight,
                            onValueChange = { inputWeight = it },
                            modifier = Modifier.weight(1f)
                        )
                        // 体脂率输入框
                        CardInputItem(
                            label = "体脂率 (%)",
                            value = inputFat,
                            onValueChange = { inputFat = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // 【改动】保存按钮 (直接调用 onSaveMeasurementsOnly)
                    Button(
                        onClick = onSaveMeasurementsOnly,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent), // 按钮底色改成强调色，更显眼
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text("保存记录并分析", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // --- 底部提示 ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timeline, contentDescription = null, tint = CyanAccent.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "“坚持记录，AI算法将为您揭示潜藏的进步趋势。”",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = TextGray,
                    lineHeight = 20.sp
                )
            }
            Spacer(Modifier.height(40.dp))
        }
    }

    // 编辑弹窗 (保持不变，用于编辑昵称年龄等)
    if (showEditDialog) {
        EditFullProfileDialog(
            initialName = name,
            initialAge = age,
            initialHeight = height,
            initialGender = gender,
            initialWeight = inputWeight, // 弹窗打开时，使用当前卡片上输入的值
            initialFat = inputFat,
            onDismiss = { showEditDialog = false },
            onConfirm = onSaveFullProfile
        )
    }
}

// --- 自定义组件 ---

@Composable
fun StyledCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardCyanBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        content = content
    )
}

@Composable
fun StyledProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextGray, fontWeight = FontWeight.Medium)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark)
    }
}

// 【新增】卡片内的输入框组件样式
@Composable
fun CardInputItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextGray)
        Spacer(Modifier.height(8.dp))
        // 定制样式的输入框，使其在卡片内更协调
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = TextDark, textAlign = androidx.compose.ui.text.style.TextAlign.Center),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyanAccent,
                unfocusedBorderColor = CyanAccent.copy(alpha = 0.3f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun EditFullProfileDialog(
    initialName: String,
    initialAge: String,
    initialHeight: String,
    initialGender: String,
    initialWeight: String,
    initialFat: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var age by remember { mutableStateOf(initialAge) }
    var height by remember { mutableStateOf(initialHeight) }
    var gender by remember { mutableStateOf(initialGender) }
    var weight by remember { mutableStateOf(initialWeight) }
    var fat by remember { mutableStateOf(initialFat) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("编辑资料", fontWeight = FontWeight.Bold, color = TextDark) },
        containerColor = CardCyanBg,
        textContentColor = TextDark,
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 昵称，性别，年龄，身高 (保持不变)...
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("昵称") }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanAccent, focusedLabelColor = CyanAccent), modifier = Modifier.fillMaxWidth())

                Column {
                    Text("性别", style = MaterialTheme.typography.bodyMedium, color = TextGray)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { gender = "male" }) {
                            RadioButton(selected = gender == "male", onClick = { gender = "male" }, colors = RadioButtonDefaults.colors(selectedColor = CyanAccent))
                            Text("男", modifier = Modifier.padding(start = 4.dp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { gender = "female" }) {
                            RadioButton(selected = gender == "female", onClick = { gender = "female" }, colors = RadioButtonDefaults.colors(selectedColor = CyanAccent))
                            Text("女", modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("年龄") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanAccent, focusedLabelColor = CyanAccent))
                    OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("身高(cm)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanAccent, focusedLabelColor = CyanAccent))
                }

                HorizontalDivider(color = CyanAccent.copy(alpha = 0.2f))

                Text("同步修改今日数据", style = MaterialTheme.typography.titleSmall, color = CyanAccent, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("体重(kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanAccent, focusedLabelColor = CyanAccent))
                    OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("体脂(%)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanAccent, focusedLabelColor = CyanAccent))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, age, height, gender, weight, fat) },
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
            ) {
                Text("保存全部", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消", color = TextGray)
            }
        }
    )
}