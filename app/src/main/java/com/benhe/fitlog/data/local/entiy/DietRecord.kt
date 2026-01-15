package com.benhe.fitlog.data.local.entiy

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * **饮食记录表 (Diet Record)**
 *
 * 这张表用于存储用户的每一条饮食记录流水账。
 * 无论是一顿正餐、一杯饮料还是一份零食，都会作为一条独立的记录存储在这里。
 *
 * @property id 记录的唯一 ID（主键）。由数据库自动生成自增的 Int 类型整数。
 * @property foodName 食物的名称，例如 "燕麦片"、"鸡胸肉"。
 * @property category 食物的类别，例如 "早餐"、"午餐"、"加餐"。用于分类统计和展示。
 * @property quantity 食物的摄入量描述，例如 "100g"、"1碗"。这是一个用于显示的字符串字段。
 * @property calories 该条记录所含的能量（卡路里，kcal）。用于计算每日总摄入量。
 * @property protein 该条记录所含的蛋白质含量（克，g）。
 * @property carbs 该条记录所含的碳水化合物含量（克，g）。
 * @property date 记录所属的日期字符串，格式通常为 "YYYY-MM-DD"。用于按天查询和汇总。
 * @property timestamp 记录创建或发生时的精确时间戳（毫秒）。用于记录的排序（例如按时间先后展示一天的饮食）。
 */
@Entity(tableName = "diet_records")
data class DietRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodName: String,
    val category: String,
    val quantity: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val date: String,
    val timestamp: Long
)