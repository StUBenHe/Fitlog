package com.benhe.fitlog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.data.FoodCatalog
import com.benhe.fitlog.model.FoodCategory
import com.benhe.fitlog.model.FoodItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen(date: String, onBack: () -> Unit) {
    // 状态管理
    var step by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf<FoodCategory?>(null) }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var amount by remember { mutableDoubleStateOf(1.0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when(step) {
                            0 -> "选择分类"
                            1 -> selectedCategory?.name ?: ""
                            else -> "确认数量"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { if (step == 0) onBack() else step-- }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier.padding(padding).fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (step) {
                0 -> { // 步骤 0：展示分类列表
                    LazyColumn {
                        items(FoodCatalog.categories) { category ->
                            ListItem(
                                headlineContent = { Text(category.name, fontSize = 18.sp) },
                                modifier = Modifier.clickable {
                                    selectedCategory = category
                                    step = 1
                                }
                            )
                            Divider(thickness = 0.5.dp, color = Color.LightGray)
                        }
                    }
                }
                1 -> { // 步骤 1：展示食物列表
                    selectedCategory?.let { category ->
                        LazyColumn {
                            items(category.items) { food ->
                                ListItem(
                                    headlineContent = { Text(food.name) },
                                    supportingContent = { Text(food.displayNote) },
                                    trailingContent = { Text("${food.kcalPerUnit} kcal", color = MaterialTheme.colorScheme.primary) },
                                    modifier = Modifier.clickable {
                                        selectedFood = food
                                        amount = food.defaultStep
                                        step = 2
                                    }
                                )
                                Divider(thickness = 0.5.dp, color = Color.LightGray)
                            }
                        }
                    }
                }
                2 -> { // 步骤 2：加减数量
                    selectedFood?.let { food ->
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(food.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Text(food.displayNote, color = Color.Gray, fontSize = 16.sp)

                            Spacer(Modifier.height(40.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                FilledTonalButton(onClick = { if (amount > 0) amount -= food.defaultStep }) {
                                    Text("-", fontSize = 24.sp)
                                }

                                Text(
                                    text = "${amount} ${food.unit}",
                                    modifier = Modifier.padding(horizontal = 32.dp),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                FilledTonalButton(onClick = { amount += food.defaultStep }) {
                                    Text("+", fontSize = 24.sp)
                                }
                            }

                            Spacer(Modifier.height(40.dp))

                            val totalKcal = (food.kcalPerUnit * amount).toInt()
                            Text("预估热量: $totalKcal kcal", fontSize = 22.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                            Spacer(Modifier.height(56.dp))

                            Button(
                                onClick = { onBack() }, // 暂时直接返回
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text("确认添加", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}