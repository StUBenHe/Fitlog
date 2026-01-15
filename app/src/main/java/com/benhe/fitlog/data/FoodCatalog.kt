package com.benhe.fitlog.data

import com.benhe.fitlog.model.FoodCategory
import com.benhe.fitlog.model.FoodItem

object FoodCatalog {
    val categories = listOf(
        FoodCategory(
            id = "carbs",
            name = "碳水",
            items = listOf(
                // 参数顺序: id, name, unit, reference, kcal, protein, fat, carbs
                FoodItem("rice", "大米", "g", "一拳头熟米饭约150g", 116.0, 2.6, 0.3, 25.9),
                FoodItem("bread", "面包", "g", "一片普通吐司约35g", 265.0, 9.0, 3.2, 49.0),
                FoodItem("pasta", "意大利面", "g", "一盘意面约250g", 151.0, 5.0, 1.1, 30.0),
                FoodItem("instant_noodle", "泡面", "g", "一饼约85g", 470.0, 9.0, 19.0, 63.0),
                FoodItem("potato", "土豆", "g", "一个中等大小约150g", 77.0, 2.0, 0.1, 17.0),
                FoodItem("sweet_potato", "红薯", "g", "一个中等大小约120g", 86.0, 1.6, 0.1, 20.0)
            )
        ),
        FoodCategory(
            id = "protein",
            name = "蛋白质",
            items = listOf(
                FoodItem("chicken_breast", "鸡胸肉", "g", "一副约150-200g", 165.0, 31.0, 3.6, 0.0),
                FoodItem("chicken_leg", "鸡腿肉", "g", "一只大鸡腿约150g", 210.0, 24.0, 12.0, 0.0),
                FoodItem(
                    "chicken_leg_no_skin",
                    "去皮鸡腿",
                    "g",
                    "去皮更低脂",
                    120.0,
                    20.0,
                    4.0,
                    0.0
                ),
                FoodItem("beef", "牛肉", "g", "一张名片大小约50g", 250.0, 26.0, 15.0, 0.0),
                FoodItem("fish", "一般鱼肉", "g", "一整条黄鱼约200g", 100.0, 18.0, 2.5, 0.0),
                FoodItem("salmon", "三文鱼", "g", "两指宽约60g", 208.0, 20.0, 13.0, 0.0),
                FoodItem("pork_tenderloin", "猪里脊", "g", "瘦肉为主", 143.0, 26.0, 4.0, 0.0),
                FoodItem("pork_ribs", "猪排骨", "g", "三四块约100g", 280.0, 18.0, 23.0, 0.0),
                FoodItem("egg", "鸡蛋", "个", "一个普通鸡蛋约50g", 70.0, 6.0, 4.8, 0.6),
                FoodItem("milk", "牛奶", "ml", "200ml约一盒/500ml约一瓶", 60.0, 3.2, 3.2, 4.8),
                FoodItem("cheese", "奶酪", "g", "一片约20g", 400.0, 25.0, 33.0, 1.3)
            )
        ),
        FoodCategory(
            id = "vitamin",
            name = "维生素",
            items = listOf(
                FoodItem("lettuce", "生菜", "g", "一颗约200g", 15.0, 1.4, 0.2, 2.9),
                FoodItem("broccoli", "西兰花", "g", "一盘约200g", 34.0, 2.8, 0.4, 7.0),
                FoodItem("tomato", "番茄", "g", "一个约150g", 18.0, 0.9, 0.2, 3.9),
                FoodItem("cucumber", "黄瓜", "g", "一根约150g", 15.0, 0.7, 0.1, 3.6),
                FoodItem("mushroom", "蘑菇", "g", "一捧约100g", 22.0, 3.0, 0.3, 3.0)
            )
        )
    )
}