package com.benhe.fitlog.model


// 食物单体
data class FoodItem(
    val id: String,
    val name: String,
    val unit: String,
    val displayNote: String,
    val kcalPerUnit: Double,
    val proteinPerUnit: Double,
    val defaultStep: Double
)

// 食物分类
data class FoodCategory(
    val id: String,
    val name: String,
    val items: List<FoodItem>
)