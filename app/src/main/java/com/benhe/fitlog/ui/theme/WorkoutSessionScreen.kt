package com.benhe.fitlog.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.model.BodyRegion
import com.benhe.fitlog.viewmodel.MainViewModel
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(date: String, viewModel: MainViewModel, onBack: () -> Unit) {
    // 1. 观察数据库。注意：确保 ViewModel 里的 getSetsByDate 逻辑正确
    val todaySets by viewModel.getSetsByDate(date).collectAsState(initial = emptyList())

    // 2. 草稿箱
    val draftState = remember { mutableStateMapOf<BodyRegion, Pair<Int, String>>() }

    // 3. 关键回显逻辑：只有当草稿箱为空且数据库有数据时才同步，避免覆盖用户正在输入的内容
    LaunchedEffect(todaySets) {
        if (todaySets.isNotEmpty() && draftState.isEmpty()) {
            todaySets.forEach { set ->
                draftState[set.region] = Pair(set.rpe ?: 0, set.note ?: "")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$date 训练记录", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "返回") } },
                actions = {
                    // 右上角也可以放个重置按钮，清空当天全部
                    TextButton(onClick = { draftState.clear() }) {
                        Text("全部清空", color = Color.Red)
                    }
                }
            )
        },
        bottomBar = {
            Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        viewModel.syncWorkoutSets(date, draftState.toMap())
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("保存记录", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {
            item { Spacer(modifier = Modifier.height(10.dp)) }
            items(BodyRegion.entries) { region ->
                val state = draftState[region] ?: Pair(0, "")
                WorkoutRegionCard(
                    regionName = region.displayName,
                    stars = state.first,
                    note = state.second,
                    onUpdate = { s, n -> draftState[region] = Pair(s, n) },
                    onDelete = { draftState.remove(region) } // 删除该部位记录
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun WorkoutRegionCard(
    regionName: String,
    stars: Int,
    note: String,
    onUpdate: (Int, String) -> Unit,
    onDelete: () -> Unit // 删除回调
) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = regionName, fontWeight = FontWeight.Bold)
                    // 删除/清空小按钮
                    if (stars > 0 || note.isNotEmpty()) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Text("✕", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
                // 星星逻辑... (保持不变)
                Row {
                    repeat(5) { i ->
                        val s = i + 1
                        Text(
                            text = if (s <= stars) "★" else "☆",
                            modifier = Modifier.clickable { onUpdate(if(stars == s) 0 else s, note) }.padding(2.dp),
                            color = if (s <= stars) Color(0xFFE67E22) else Color.LightGray,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { onUpdate(stars, it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("记录数据...", fontSize = 12.sp) }
            )
        }
    }
}


@Composable
fun StarRatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        repeat(5) { index ->
            val starIndex = index + 1
            Text(
                text = if (starIndex <= rating) "★" else "☆",
                fontSize = 22.sp,
                color = if (starIndex <= rating) Color(0xFFE67E22) else Color(0xFFBDC3C7),
                modifier = androidx.compose.ui.Modifier
                    .padding(horizontal = 2.dp)
                    .clickable { onRatingChanged(starIndex) }
            )
        }
    }
}