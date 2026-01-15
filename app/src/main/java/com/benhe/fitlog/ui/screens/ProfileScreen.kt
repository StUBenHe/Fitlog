package com.benhe.fitlog.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(onNavigateToCalendar: () -> Unit) {
    val context = LocalContext.current

    // 输入状态
    var username by remember { mutableStateOf("") } // 用户名
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("male") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // 加上滚动，防止小屏幕键盘遮挡
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "建立你的健身档案", style = MaterialTheme.typography.headlineMedium)

        // --- 用户名输入 ---
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("怎么称呼你？") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("例如：Ben") }
        )

        // --- 性别选择 ---
        Text(text = "性别", style = MaterialTheme.typography.bodyLarge)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = (gender == "male"), onClick = { gender = "male" })
            Text("男", modifier = Modifier.padding(end = 16.dp))
            RadioButton(selected = (gender == "female"), onClick = { gender = "female" })
            Text("女")
        }

        // --- 体重、身高 ---
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = weight, onValueChange = { weight = it },
                label = { Text("体重(kg)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = height, onValueChange = { height = it },
                label = { Text("身高(cm)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // --- 年龄 ---
        OutlinedTextField(
            value = age, onValueChange = { age = it },
            label = { Text("年龄") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // --- 体脂率 ---
        OutlinedTextField(
            value = bodyFat, onValueChange = { bodyFat = it },
            label = { Text("体脂率 % (可选)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // 判空校验
                if (username.isNotEmpty() && weight.isNotEmpty() && height.isNotEmpty() && age.isNotEmpty()) {
                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

                    with(sharedPref.edit()) {
                        // ✅ 关键修改：这里的 Key 必须和 RightProfileScreen 里读取的一致
                        putString("name", username)      // 之前是 "username"，改为 "name"
                        putString("gender", gender)
                        putString("weight", weight)
                        putString("height", height)
                        putString("age", age)
                        putString("bodyFat", bodyFat)    // 之前是 "body_fat"，改为 "bodyFat"
                        putBoolean("has_init", true)
                        apply()
                    }

                    Toast.makeText(context, "档案创建成功！", Toast.LENGTH_SHORT).show()
                    onNavigateToCalendar()
                } else {
                    Toast.makeText(context, "除了体脂，其他都是必填的哦", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("开启健身之旅")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}