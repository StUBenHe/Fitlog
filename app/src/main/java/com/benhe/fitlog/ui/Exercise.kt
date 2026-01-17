package com.benhe.fitlog.ui


import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
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

// ✅ 新增：虚线边框修饰符（用于添加按钮）
// ✅ 修改后的虚线边框修饰符
fun Modifier.dashedBorder(width: Dp, color: Color, shape: Shape): Modifier = this.drawBehind {
    val stroke = Stroke(
        width = width.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
    )
    val outline = shape.createOutline(size, layoutDirection, this)

    // ✅ 修复点：使用命名参数 style = stroke
    drawOutline(
        outline = outline,
        color = color,
        style = stroke
    )
}
//================== 核心組件：身體部位行 (小修改) ====================

@Composable
fun GlassBodyRegionRow(
    regionName: String,
    level: Int,
    note: String,
    onLevelClick: () -> Unit,
    onNoteChange: (String) -> Unit,
    onRemove: () -> Unit // ✅ 修改：现在的含义是“移除”，而不是“清除数据”
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. 部位名稱
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
            Brush.horizontalGradient(listOf(Sky500, Cyan500))
        } else {
            SolidColor(Color.White.copy(alpha = 0.3f))
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
                        Icons.Outlined.Bolt,
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

        // 4. 移除按钮 (X)
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(24.dp)
                .background(Color.White.copy(alpha = 0.5f), CircleShape)
                .border(1.dp, Color.White, CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                tint = Rose400, // 移除通常用红色警示
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ==================== 主畫面 ====================

@Composable
fun WorkoutSessionScreen(date: String, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val regionState = viewModel.regionDraftState

    // ✅ 新增：控制添加对话框显示的 State
    var showAddDialog by remember { mutableStateOf(false) }

    // ✅ 新增：计算尚未添加的部位列表
    // 通过过滤所有部位，找出不在 regionState 的 key 中的部位
    val availableRegions = remember(regionState.size) {
        BodyRegion.entries.filter { !regionState.containsKey(it) }
    }

    LaunchedEffect(date) {
        viewModel.loadWorkoutDraftsForDate(date)
    }

    // --- 添加部位的对话框 ---
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    "选择要添加的部位",
                    fontWeight = FontWeight.Bold,
                    color = TextSlate800
                )
            },
            text = {
                if (availableRegions.isEmpty()) {
                    Text("所有部位已添加完毕", color = TextSlate600)
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableRegions) { region ->
                            Button(
                                onClick = {
                                    // 点击后，向状态 Map 中添加该部位，使用默认空值
                                    regionState[region] = Pair(0, "")
                                    showAddDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Teal50.copy(alpha = 0.8f),
                                    contentColor = Teal500
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(region.displayName, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("关闭", color = TextSlate400)
                }
            },
            containerColor = GlassWhite,
            shape = RoundedCornerShape(24.dp)
        )
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

                // 統一表頭 (如果列表为空则不显示)
                if (regionState.isNotEmpty()) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)) {
                        Text("部位", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSlate400, modifier = Modifier.weight(2.5f))
                        Text("等級", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSlate400, modifier = Modifier.weight(2.5f), textAlign = TextAlign.Center)
                        Text("備註", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSlate400, modifier = Modifier.weight(2.5f))
                        Spacer(modifier = Modifier.width(24.dp))
                    }
                }

                // 列表內容
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // ✅ 修改：只遍历已经添加到状态中的部位 (regionState.keys)
                    // 将 keys 转为 list 并排序，保证显示顺序稳定（例如按枚举定义顺序）
                    items(regionState.keys.toList().sortedBy { it.ordinal }) { region ->
                        val state = regionState[region] ?: Pair(0, "")
                        val (level, note) = state
                        GlassBodyRegionRow(
                            regionName = region.displayName,
                            level = level,
                            note = note,
                            onLevelClick = {
                                val newLevel = if (level >= 5) 0 else level + 1
                                regionState[region] = state.copy(first = newLevel)
                            },
                            onNoteChange = { newNote ->
                                regionState[region] = state.copy(second = newNote)
                            },
                            // ✅ 修改：点击 X 是从 Map 中彻底移除该部位
                            onRemove = {
                                regionState.remove(region)
                            }
                        )
                    }

                    // ✅ 新增：底部的“添加部位”按钮（仿照截图样式）
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                // 使用自定义的虚线边框
                                .dashedBorder(width = 1.dp, color = Teal500.copy(alpha = 0.5f), shape = RoundedCornerShape(24.dp))
                                .clip(RoundedCornerShape(24.dp))
                                .background(Teal50.copy(alpha = 0.3f))
                                .clickable {
                                    // 点击打开对话框
                                    showAddDialog = true
                                }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Teal500)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("添加训练部位", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Teal500)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // --- 底部保存按鈕 (保持不变) ---
                Box(modifier = Modifier.padding(24.dp)) {
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.syncWorkoutSets(
                                    dateString = date,
                                    drafts = regionState.toMap()
                                )
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