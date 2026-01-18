@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.benhe.fitlog.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.LocalDrink

import androidx.compose.material3.MaterialTheme

// ---------------------------
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.benhe.fitlog.model.FoodCategory
import com.benhe.fitlog.model.FoodItem
import com.benhe.fitlog.viewmodel.MainViewModel

// ... 后面的代码保持不变

// ==================== 通用 UI 組件 ====================

// 毛玻璃卡片容器
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.9f)),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

// 圆润的食物标签组件
@Composable
fun FoodTag(
    text: String,
    isSelected: Boolean,
    themeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = if (isSelected) themeColor else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE0E0E0)),
        shadowElevation = if (isSelected) 4.dp else 2.dp,
        modifier = Modifier.height(40.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}

// 用于存储分类样式信息的数据类
data class CategoryStyle(val color: Color, val icon: ImageVector)

// 获取分类样式的辅助函数
@Composable
fun getCategoryStyle(categoryId: String): CategoryStyle {
    return when (categoryId) {
        "carbs" -> CategoryStyle(
            Color(0xFFFF6B6B),
            Icons.Default.Restaurant
        ) // 主食与谷物

        "protein" -> CategoryStyle(
            Color(0xFFFFD93D),
            Icons.Default.Egg
        ) // 肉蛋奶与豆制品

        "veggies" -> CategoryStyle(
            Color(0xFF6BCB77),
            Icons.Default.Grass
        ) // 蔬菜与菌菇

        "fruits" -> CategoryStyle(
            Color(0xFF4D96FF),
            Icons.Default.EmojiFoodBeverage
        ) // 水果

        "fats" -> CategoryStyle(
            Color(0xFFC9A227), // 暖金色
            Icons.Default.Opacity
        )

        "drinks_misc" -> CategoryStyle(
            Color(0xFF845EC2),
            Icons.Default.LocalDrink
        ) // 饮料与调味品

        "custom_user" -> CategoryStyle(
            Color(0xFF9C27B0),
            Icons.Default.Edit
        ) // 用户自定义

        else -> CategoryStyle(
            MaterialTheme.colorScheme.primary,
            Icons.Default.Fastfood
        )
    }
}


// 修改后的圆形分类 Tab 组件
@Composable
fun CategoryTab(
    category: FoodCategory,
    isSelected: Boolean,
    style: CategoryStyle, // 接收样式信息
    onClick: () -> Unit
) {
    // 定义未选中状态的统一灰色
    val unselectedColor = Color(0xFFF0F0F0)
    val unselectedTextColor = Color.Gray

    // 根据选中状态决定当前显示的颜色
    val currentColor = if (isSelected) style.color else unselectedColor
    val currentIconColor = if (isSelected) Color.White else unselectedTextColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(currentColor) // 使用计算出的颜色
                .border(
                    // 未选中时显示边框，选中时不显示
                    width = if (isSelected) 0.dp else 1.dp,
                    color = if (isSelected) Color.Transparent else unselectedTextColor.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // 使用 Icon 替代之前的 Text
            Icon(
                imageVector = style.icon,
                contentDescription = category.name,
                tint = currentIconColor,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) style.color else unselectedTextColor // 选中显示主题色，未选中显示灰色
        )
    }
}

// ==================== 主界面 ====================

@Composable
fun DietScreen(
    date: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val categories by viewModel.allFoodCategories.collectAsState()

    if (categories.isEmpty()) return

    var selectedCatIndex by remember { mutableIntStateOf(0) }
    val safeIndex = selectedCatIndex.coerceIn(0, categories.lastIndex)
    val currentCategory = categories[safeIndex]

    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantityStr by remember { mutableStateOf("100") }
    var showAddDialog by remember { mutableStateOf(false) }

    // 获取当前选中分类的样式
    val currentStyle = getCategoryStyle(currentCategory.id)
    val themeColor = currentStyle.color

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        // ✅ 关键修改：设置 contentWindowInsets 为 0，不让 Scaffold 自动处理系统窗口边距
        // 这样它就不会延伸到底部导航栏区域下面
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("录入饮食 - $date", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .shadow(2.dp, CircleShape)
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .shadow(2.dp, CircleShape)
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "更多", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("选择食物分类", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // 1. 圆形分类 Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                categories.forEachIndexed { index, cat ->
                    val isSelected = safeIndex == index
                    // 获取每个分类的样式
                    val style = getCategoryStyle(cat.id)

                    CategoryTab(
                        category = cat,
                        isSelected = isSelected,
                        style = style, // 传递样式对象
                        onClick = { selectedCatIndex = index; selectedFood = null }
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. 食物选择卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("选择${currentCategory.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.Info, contentDescription = "信息", tint = Color.Gray)
                    }
                    Spacer(Modifier.height(20.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        currentCategory.items.forEach { food ->
                            val isSelected = selectedFood?.id == food.id
                            FoodTag(
                                text = food.name,
                                isSelected = isSelected,
                                themeColor = themeColor,
                                onClick = {
                                    selectedFood = food
                                    quantityStr = if(food.unit == "个" || food.unit == "片") "1" else "100"
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = themeColor.copy(alpha = 0.1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { showAddDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = themeColor)
                            Spacer(Modifier.width(8.dp))
                            Text("没找到？添加自定义食物", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = themeColor)
                        }
                    }
                }
            }

            // 4. 详情录入与保存区域
            selectedFood?.let { food ->
                val qty = quantityStr.toDoubleOrNull() ?: 0.0
                val factor = if(food.unit == "个" || food.unit == "片") qty else qty / 100.0
                val curCal = (food.kcalPerUnit * factor).toInt()
                val curPro = String.format("%.1f", food.proteinPerUnit * factor)
                val curCarb = String.format("%.1f", food.carbsPerUnit * factor)

                Spacer(modifier = Modifier.height(24.dp))

                GlassCard {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = themeColor.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("💡 参照：${food.reference}", color = themeColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "当前预览：$curCal kcal | 蛋白质 ${curPro}g | 碳水 ${curCarb}g",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = quantityStr,
                        onValueChange = { quantityStr = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("录入数量 (${food.unit})") },
                        suffix = { Text(food.unit, fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = themeColor,
                            focusedLabelColor = themeColor,
                            cursorColor = themeColor
                        ),
                        textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                    shape = RoundedCornerShape(32.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text("确认添加", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showAddDialog) {
        AddFoodDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newItem ->
                viewModel.addCustomFood(newItem)
                val customIndex = categories.indexOfFirst { it.id == "custom_user" }
                if (customIndex != -1) {

                    selectedFood = newItem
                }

            }
        )
    }
}

// ================== 组件：添加食物弹窗 (保持不变) ==================
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
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("添加自定义食物", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("食物名称 (如: 燕麦)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = kcal,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) kcal = it },
                        label = { Text("热量/100$unit") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("单位") },
                        modifier = Modifier.width(90.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("蛋白") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("碳水") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        label = { Text("脂肪") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Text("注：营养素请输入每100单位含量的数值", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Spacer(modifier = Modifier.height(8.dp))

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
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("保存") }
                }
            }
        }
    }
}