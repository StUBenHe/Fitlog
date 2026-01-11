package com.benhe.fitlog.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.data.FoodCatalog
import com.benhe.fitlog.model.FoodItem
import com.benhe.fitlog.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DietScreen(
    date: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val categories = FoodCatalog.categories
    var selectedCatIndex by remember { mutableIntStateOf(0) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var quantityStr by remember { mutableStateOf("100") }

    val currentCategory = categories[selectedCatIndex]
    val themeColor = when(currentCategory.id) {
        "carbs" -> Color(0xFFEF5350)
        "protein" -> Color(0xFFFFB300)
        "vitamin" -> Color(0xFF66BB6A)
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
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // 分类切换
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                categories.forEachIndexed { index, cat ->
                    val isSelected = selectedCatIndex == index
                    val catColor = when(cat.id) {
                        "carbs" -> Color(0xFFEF5350)
                        "protein" -> Color(0xFFFFB300)
                        "vitamin" -> Color(0xFF66BB6A)
                        else -> Color.Gray
                    }
                    Button(
                        onClick = { selectedCatIndex = index; selectedFood = null },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) catColor else catColor.copy(alpha = 0.15f),
                            contentColor = if (isSelected) Color.White else catColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text(cat.name, fontWeight = FontWeight.Bold) }
                }
            }

            // 食物选择
            Text("选择${currentCategory.name}", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                currentCategory.items.forEach { food ->
                    val isSelected = selectedFood?.id == food.id
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedFood = food
                            quantityStr = if(food.unit == "个") "1" else "100"
                        },
                        label = { Text(food.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = themeColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // 详情与保存
            selectedFood?.let { food ->
                // 实时预览计算
                val qty = quantityStr.toDoubleOrNull() ?: 0.0
                val factor = if(food.unit == "个") qty else qty / 100.0
                val curCal = (food.kcalPerUnit * factor).toInt()
                val curPro = (food.proteinPerUnit * factor).toInt()
                val curCarb = (food.carbsPerUnit * factor).toInt()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = themeColor.copy(alpha = 0.05f)),
                    border = BorderStroke(1.dp, themeColor.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💡 参照：${food.reference}", color = themeColor, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("当前预览：$curCal kcal | 蛋白质 ${curPro}g | 碳水 ${curCarb}g", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = quantityStr,
                    onValueChange = { quantityStr = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("录入数量 (${food.unit})") },
                    suffix = { Text(food.unit) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.saveDietRecord(
                            foodName = food.name,
                            category = currentCategory.name,
                            quantity = "$quantityStr${food.unit}",
                            calories = food.kcalPerUnit * factor,
                            protein = food.proteinPerUnit * factor,
                            carbs = food.carbsPerUnit * factor, // ✅ 修复：传入计算后的碳水
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
}


@Composable
fun NutrientMiniItem(x0: String, x1: String, x2: Color) {
    TODO("Not yet implemented")
}