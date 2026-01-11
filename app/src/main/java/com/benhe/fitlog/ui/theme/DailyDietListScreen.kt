package com.benhe.fitlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.data.db.DietRecord
import com.benhe.fitlog.viewmodel.MainViewModel
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyDietListScreen(
    date: String,
    viewModel: MainViewModel,
    onAddClick: () -> Unit,
    onBack: () -> Unit // 建议增加返回按钮回调
) {
    val records by viewModel.getDietRecordsForDate(date).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("$date 饮食清单") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            // ... 你的 DailyDietListScreen 逻辑 ...
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(records) { record -> DietItemRow(record) }
                item {
                    TextButton(
                        onClick = onAddClick,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text("添加饮食")
                    }
                }
            }
        }
    }
}

@Composable
fun DietItemRow(record: DietRecord) {
    // ... 你的 DietItemRow 逻辑 ...
}