package com.benhe.fitlog.data


import com.benhe.fitlog.model.FoodCategory
import com.benhe.fitlog.model.FoodItem

object FoodCatalog {
    val categories = listOf(
        FoodCategory(
            id = "protein",
            name = "蛋白质",
            items = listOf(
                FoodItem("egg", "鸡蛋", "个", "1 个 ≈ 50g", 70.0, 6.0, 1.0),
                FoodItem("milk", "牛奶", "杯", "1 杯 ≈ 250ml", 150.0, 8.0, 0.5),
                // ... 后面的以此类推
                FoodItem("tofu", "豆腐", "块", "1 块 ≈ 100g", 80.0, 8.0, 0.5)
            )
        ),
        // ... 其他分类
    )
}