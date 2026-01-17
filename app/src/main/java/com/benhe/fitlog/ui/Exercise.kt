package com.benhe.fitlog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benhe.fitlog.model.BodyRegion
import com.benhe.fitlog.ui.theme.*
import com.benhe.fitlog.viewmodel.MainViewModel
import kotlinx.coroutines.launch

// ==================== 通用 UI 組件 ====================

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(32.dp),
    elevation: Dp = 2.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation, shape, spotColor = Color.Black.copy(alpha = 0.05f))
            .clip(shape)
            .background(GlassWhite)
            .border(1.dp, GlassBorder, shape),
        content = content
    )
}

@Composable
fun GlassInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    centerAlign: Boolean = false,
    readOnly: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = if (centerAlign) Alignment.Center else Alignment.CenterStart
    ) {
        if (value.isEmpty() && !readOnly) {
            Text(
                text = placeholder,
                style = textStyle.copy(color = TextSlate400.copy(alpha = 0.7f)),
                textAlign = if (centerAlign) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            cursorBrush = SolidColor(Brand500),
            singleLine = true,
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (centerAlign) {
                    Box(contentAlignment = Alignment.Center) { innerTextField() }
                } else {
                    innerTextField()
                }
            }
        )
    }
}

// ==================== 核心組件：身體部位行 ====================

@Composable
fun GlassBodyRegionRow(
    regionName: String,
    level: Int,
    note: String,
    onLevelClick: () -> Unit, // 點擊切換等級
    onNoteChange: (String) -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. 部位名稱 (靜態顯示)
        GlassInput(
            value = regionName,
            onValueChange = {},
            placeholder = "",
            readOnly = true,
            modifier = Modifier.weight(2.5f),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSlate800
            )
        )

        // 2. 等級徽章選擇器
        val isSelected = level > 0
        val backgroundStyle = if (isSelected) {
            Brush.horizontalGradient(listOf(Sky500, Cyan500)) // 選中時的漸變背景
        } else {
            SolidColor(Color.White.copy(alpha = 0.3f)) // 未選中時的半透明背景
        }
        val textColor = if (isSelected) Color.White else TextSlate400

        Box(
            modifier = Modifier
                .weight(2.5f)
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundStyle)
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .clickable(onClick = onLevelClick)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = if (level > 0) "等级 LVL $level" else "選擇等級",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                if (isSelected) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Outlined.Bolt, // 閃電圖標
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // 3. 備註輸入
        GlassInput(
            value = note,
            onValueChange = onNoteChange,
            placeholder = "Notes...",
            modifier = Modifier.weight(2.5f),
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = TextSlate600,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        )

        // 4. 清除按鈕
        val hasData = level > 0 || note.isNotEmpty()
        IconButton(
            onClick = onClear,
            enabled = hasData,
            modifier = Modifier
                .size(24.dp)
                .background(
                    if(hasData) Color.White.copy(alpha = 0.5f) else Color.Transparent,
                    CircleShape
                )
                .border(1.dp, if(hasData) Color.White else Color.Transparent, CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Clear",
                tint = if (hasData) Rose400 else TextSlate400.copy(alpha = 0.3f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ==================== 主畫面 ====================

@Composable
fun WorkoutSessionScreen(date: String, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()

    // --- 核心修改 1：直接使用 ViewModel 中的状态 ---
    // 這樣狀態就能在頁面重建時保留，並在不同頁面間共享
    val regionState = viewModel.regionDraftState

    // --- 核心修改 2：進入頁面時加載數據 ---
    // 使用 LaunchedEffect 在 date 變化時（即進入新頁面時）從數據庫加載數據
    LaunchedEffect(date) {
        viewModel.loadWorkoutDraftsForDate(date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 100.dp)
    ) {
        // --- 頂部標題欄 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Teal500)
                        .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = Teal500.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color.White)
                }
                Column {
                    Text("训练记录", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = TextSlate800)
                    Text(date, style = MaterialTheme.typography.bodySmall, color = TextSlate600)
                }
            }
            TextButton(onClick = {
                // 清空 ViewModel 中的状态
                regionState.clear()
            }) {
                Text("清空全部", color = TextSlate400, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 主要內容卡片 ---
        GlassContainer(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(40.dp),
            elevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // 卡片標題
                Row(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Teal500, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("詳細日誌", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextSlate800)
                }

                // 統一表頭
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)) {
                    Text("部位", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSlate400, modifier = Modifier.weight(2.5f))
                    Text("等級", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSlate400, modifier = Modifier.weight(2.5f), textAlign = TextAlign.Center)
                    Text("備註", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSlate400, modifier = Modifier.weight(2.5f))
                    Spacer(modifier = Modifier.width(24.dp))
                }

                // 列表內容
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // --- 身體部位列表 ---
                    items(BodyRegion.entries) { region ->
                        // 直接從 ViewModel 的狀態中讀取
                        val state = regionState[region] ?: Pair(0, "")
                        val (level, note) = state
                        GlassBodyRegionRow(
                            regionName = region.displayName,
                            level = level,
                            note = note,
                            onLevelClick = {
                                // 點擊循環切換等級 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 0
                                val newLevel = if (level >= 5) 0 else level + 1
                                // 直接更新 ViewModel 中的狀態
                                regionState[region] = state.copy(first = newLevel)
                            },
                            onNoteChange = { newNote ->
                                // 直接更新 ViewModel 中的狀態
                                regionState[region] = state.copy(second = newNote)
                            },
                            onClear = {
                                // 直接更新 ViewModel 中的狀態
                                regionState[region] = Pair(0, "")
                            }
                        )
                    }
                }

                // --- 底部保存按鈕 ---
                Box(modifier = Modifier.padding(24.dp)) {
                    Button(
                        onClick = {
                            scope.launch {
                                // 調用 ViewModel 方法保存，傳遞的 drafts 就是 ViewModel 自己的状态
                                viewModel.syncWorkoutSets(
                                    dateString = date,
                                    drafts = regionState.toMap() // 將 SnapshotStateMap 轉為普通 Map
                                )
                                // 這裡可以添加一個 Toast 提示或者導航回上一頁
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = Teal500.copy(alpha = 0.4f)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(32.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Teal500, Cyan500))),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("保存記錄", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}