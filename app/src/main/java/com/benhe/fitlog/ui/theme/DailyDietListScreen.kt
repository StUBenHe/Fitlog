package com.benhe.fitlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.data.db.DietRecord
import com.benhe.fitlog.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyDietListScreen(
    date: String,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onAddClick: () -> Unit
) {
    // 核心：观察数据库中的实时数据流
// 显式指定 initial = emptyList<DietRecord>()
    val dietRecords by viewModel.getDietRecordsForDate(date).collectAsState(initial = emptyList<DietRecord>())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$date 饮食清单", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("添加饮食") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    ) { padding ->
        if (dietRecords.isEmpty()) {
            // 空状态展示
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("今天还没有记录，点击右下角添加", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dietRecords) { record ->
                    DietListItem(
                        record = record,
                        onDelete = { viewModel.deleteDietRecord(record) }
                    )
                }
            }
        }
    }
}

@Composable
fun DietListItem(record: DietRecord, onDelete: () -> Unit) {
    // 根据分类确定圆点颜色
    val dotColor = when (record.category) {
        "碳水" -> Color(0xFFEF5350)
        "蛋白质" -> Color(0xFFFFB300)
        "维生素" -> Color(0xFF66BB6A)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. 颜色圆点
            Box(modifier = Modifier.size(10.dp).background(dotColor, CircleShape))

            Spacer(Modifier.width(16.dp))

            // 2. 食物名称和热量
            Column(modifier = Modifier.weight(1f)) {
                Text(text = record.foodName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "${record.quantity} | ${record.calories.toInt()} kcal | 蛋白质:${record.protein.toInt()}g",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // 3. 删除按钮
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "删除", tint = Color.LightGray)
            }
        }
    }
}