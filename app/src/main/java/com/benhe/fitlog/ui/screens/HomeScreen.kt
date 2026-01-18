package com.benhe.fitlog.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.benhe.fitlog.ui.components.DayCard
import com.benhe.fitlog.util.DateUtils
import com.benhe.fitlog.viewmodel.MainViewModel
import java.time.LocalDate
import com.benhe.fitlog.ui.theme.Emerald400
import com.benhe.fitlog.ui.theme.Brand500

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToDietList: (String) -> Unit
) {
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
    // 读取名字，注意 Key 是 "name"
    val name = sharedPref.getString("name", "朋友") ?: "朋友"
    val today = LocalDate.now()
    val dateString = today.toString()

    Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Text(
            text = "此日状态",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "👋 你好，$name!",
            style = MaterialTheme.typography.titleMedium,
            color = Brand500
        )
        Spacer(Modifier.height(20.dp))

        // 引用 ui/components/DayCard.kt 中的组件
        DayCard(
            date = dateString,
            weekday = DateUtils.getWeekday(today),
            isToday = true,
            viewModel = viewModel,
            onDietClick = { onNavigateToDietList(dateString) }
        )
    }
}