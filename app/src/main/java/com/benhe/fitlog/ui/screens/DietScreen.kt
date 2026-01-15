package com.benhe.fitlog.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.benhe.fitlog.model.FoodItem
import com.benhe.fitlog.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DietScreen(
    date: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    // 1. 改为观察 ViewModel 的数据流 (包含默认 + 自定义)
    val categories by viewModel.allFoodCategories.collectAsState()

    // 如果数据还没加载好，显示Loading或空白
    if (categories.isEmpty()) return

    // 状态管理
    var selectedCatIndex by remember { mutableIntStateOf(0) }
    // 防止分类数量变化导致索引越界
    val safeIndex = selectedCatIndex.coerceIn(0, categories.lastIndex)
    val currentCategory = categories[safeIndex]

    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantityStr by remember { mutableStateOf("100") }
    var showAddDialog by remember { mutableStateOf(false) }

    // 主题色逻辑
    val themeColor = when(currentCategory.id) {
        "carbs" -> Color(0xFFEF5350)     // 红
        "protein" -> Color(0xFFFFB300)   // 黄
        "vitamin" -> Color(0xFF66BB6A)   // 绿
        "custom_user" -> Color(0xFF9C27B0) // 自定义-紫
        else -> MaterialTheme.colorScheme.primary
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("录入饮食 - $date") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        // 使用 verticalScroll 让屏幕不够高时可以滚动
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. 顶部分类切换 Tabs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                categories.forEachIndexed { index, cat ->
                    val isSelected = safeIndex == index
                    // 根据ID给不同颜色，如果不在预设里就用灰色
                    val catColor = when(cat.id) {
                        "carbs" -> Color(0xFFEF5350)
                        "protein" -> Color(0xFFFFB300)
                        "vitamin" -> Color(0xFF66BB6A)
                        "custom_user" -> Color(0xFF9C27B0)
                        else -> Color.Gray
                    }

                    Button(
                        onClick = { selectedCatIndex = index; selectedFood = null },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) catColor else catColor.copy(alpha = 0.15f),
                            contentColor = if (isSelected) Color.White else catColor
                        ),
                        contentPadding = PaddingValues(0.dp), // 紧凑一点
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if(cat.name.length > 4) cat.name.take(2) + ".." else cat.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            // 2. 食物选择区域
            Text("选择${currentCategory.name}", style = MaterialTheme.typography.labelLarge, color = Color.Gray)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentCategory.items.forEach { food ->
                    val isSelected = selectedFood?.id == food.id
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedFood = food
                            quantityStr = if(food.unit == "个" || food.unit == "片") "1" else "100"
                        },
                        label = { Text(food.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = themeColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // 3. 【新增】自定义添加入口
            // 放在食物列表下面，作为一个补充选项
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "没找到想吃的？",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                TextButton(
                    onClick = { showAddDialog = true },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("添加自定义食物")
                }
            }

            // 4. 详情录入与保存区域 (选中食物后显示)
            selectedFood?.let { food ->
                // 实时预览计算
                val qty = quantityStr.toDoubleOrNull() ?: 0.0
                val factor = if(food.unit == "个" || food.unit == "片") qty else qty / 100.0 // 假设每单位为100g/ml

                // 格式化一下防止小数位太多
                val curCal = (food.kcalPerUnit * factor).toInt()
                val curPro = String.format("%.1f", food.proteinPerUnit * factor)
                val curCarb = String.format("%.1f", food.carbsPerUnit * factor)

                Spacer(Modifier.height(8.dp))

                // 信息卡片
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = themeColor.copy(alpha = 0.05f)),
                    border = BorderStroke(1.dp, themeColor.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💡 参照：${food.reference}", color = themeColor, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "当前预览：$curCal kcal | 蛋白质 ${curPro}g | 碳水 ${curCarb}g",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    }
                }

                // 数量输入框
                OutlinedTextField(
                    value = quantityStr,
                    onValueChange = { quantityStr = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("录入数量 (${food.unit})") },
                    suffix = { Text(food.unit) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 确认按钮
                Button(
                    onClick = {
                        viewModel.saveDietRecord(
                            foodName = food.name,
                            category = currentCategory.name,
                            quantity = "$quantityStr${food.unit}",
                            calories = food.kcalPerUnit * factor,
                            protein = food.proteinPerUnit * factor,
                            carbs = food.carbsPerUnit * factor,
                            date = date
                        )
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("确认添加", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // 5. 弹窗显示逻辑
    if (showAddDialog) {
        AddFoodDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newItem ->
                // 保存到 ViewModel (会存入SP)
                viewModel.addCustomFood(newItem)
                // 自动切换到自定义分类，并选中新添加的食物
                val customIndex = categories.indexOfFirst { it.id == "custom_user" }
                if (customIndex != -1) {
                    selectedCatIndex = customIndex
                    selectedFood = newItem
                }
                showAddDialog = false
            }
        )
    }
}

// ================== 组件：添加食物弹窗 ==================

@Composable
fun AddFoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (FoodItem) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var kcal by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("g") }
    var reference by remember { mutableStateOf("自定义添加") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("添加自定义食物", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                // 第一行：名称
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("食物名称 (如: 燕麦)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // 第二行：热量和单位
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = kcal,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) kcal = it },
                        label = { Text("热量/100$unit") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("单位") },
                        modifier = Modifier.width(80.dp)
                    )
                }

                // 第三行：三大营养素
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("蛋白") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("碳水") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        label = { Text("脂肪") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Text("注：营养素请输入每100单位含量的数值", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Spacer(modifier = Modifier.height(8.dp))

                // 按钮栏
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("取消") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotEmpty() && kcal.isNotEmpty()) {
                                val item = FoodItem(
                                    id = "custom_${System.currentTimeMillis()}",
                                    name = name,
                                    unit = unit,
                                    reference = reference,
                                    kcalPerUnit = kcal.toDoubleOrNull() ?: 0.0,
                                    proteinPerUnit = protein.toDoubleOrNull() ?: 0.0,
                                    fatPerUnit = fat.toDoubleOrNull() ?: 0.0,
                                    carbsPerUnit = carbs.toDoubleOrNull() ?: 0.0
                                )
                                onConfirm(item)
                            }
                        }
                    ) { Text("保存") }
                }
            }
        }
    }
}