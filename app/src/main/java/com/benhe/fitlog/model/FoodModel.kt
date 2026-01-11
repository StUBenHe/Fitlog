package com.benhe.fitlog.model

data class FoodItem(
    val id: String,
    val name: String,
    val unit: String,
    val reference: String,
    val kcalPerUnit: Double,
    val proteinPerUnit: Double,
    val fatPerUnit: Double = 0.0,
    val carbsPerUnit: Double // 确保这个在最后或倒数第二
)

data class FoodCategory(
    val id: String,
    val name: String,
    val items: List<FoodItem>
)