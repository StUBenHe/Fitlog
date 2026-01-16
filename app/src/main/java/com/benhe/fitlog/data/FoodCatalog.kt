package com.benhe.fitlog.data

import com.benhe.fitlog.model.FoodCategory
import com.benhe.fitlog.model.FoodItem

/**
 * 扩展后的本地食物数据库 (V3 Enhanced)
 * 注意：
 * 1. 所有的营养数据均为每 100g (或 100ml) 的大致平均值，仅供参考。
 * 2. 除非特别标注（如"熟"），默认指生重/未烹饪状态。
 * 3. 引用量 (reference) 是为了帮助用户估算，并非标准单位。
 */
object FoodCatalog {
    val categories = listOf(
        // ==================== 碳水化合物与主食 ====================
        FoodCategory(
            id = "carbs",
            name = "主食与谷物",
            items = listOf(
                // 参数顺序: id, name, unit, reference, kcal, protein, fat, carbs (per 100 unit)
                FoodItem("rice_white_raw", "大米(生)", "g", "煮熟后重量约增加2-2.5倍", 360.0, 7.0, 0.9, 77.0),
                FoodItem("rice_cooked", "米饭(熟)", "g", "一拳头大小约150g", 130.0, 2.7, 0.3, 28.0),
                FoodItem("rice_brown_raw", "糙米(生)", "g", "更健康的粗粮选择", 362.0, 7.5, 2.8, 76.0),
                FoodItem("oats_raw", "生燕麦片", "g", "一顿常用量约40-50g", 389.0, 16.9, 6.9, 66.3),
                FoodItem("whole_wheat_bread", "全麦面包", "g", "一片约35g-40g", 250.0, 10.0, 4.0, 43.0),
                FoodItem("bread", "白吐司", "g", "一片约35g", 265.0, 9.0, 3.2, 49.0),
                FoodItem("bagel", "贝果面包", "g", "一个通常重80-100g，高碳水", 275.0, 10.0, 1.5, 55.0),
                FoodItem("sweet_potato", "红薯/地瓜", "g", "一个中等大小约150g", 86.0, 1.6, 0.1, 20.0),
                FoodItem("potato", "土豆", "g", "一个中等大小约150g", 77.0, 2.0, 0.1, 17.0),
                FoodItem("corn", "甜玉米(熟)", "g", "一根中等约200g(可食部)", 106.0, 4.0, 1.5, 23.0),
                FoodItem("pasta_raw", "意大利面(干)", "g", "一人份约80g-100g", 371.0, 13.0, 1.5, 75.0),
                FoodItem("quinoa_raw", "藜麦(生)", "g", "优质植物蛋白和碳水", 368.0, 14.0, 6.0, 64.0),
                FoodItem("chickpeas_cooked", "鹰嘴豆(熟/罐头)", "g", "优质慢碳水和植物蛋白", 164.0, 9.0, 2.6, 27.0),
                FoodItem("red_beans_cooked", "红豆/腰豆(熟)", "g", "常用于沙拉或炖菜", 127.0, 9.0, 0.5, 23.0),
                FoodItem("instant_noodle", "方便面饼", "g", "一块面饼约85g，高钠预警", 470.0, 9.0, 19.0, 63.0)
            )
        ),
        // ==================== 蛋白质来源 ====================
        FoodCategory(
            id = "protein",
            name = "肉蛋奶与豆制品",
            items = listOf(
                FoodItem("whey_protein", "乳清蛋白粉", "g", "一勺通常为30g", 380.0, 78.0, 5.0, 6.0),
                FoodItem("chicken_breast", "鸡胸肉(生)", "g", "一块完整约200g，高蛋白低脂", 165.0, 31.0, 3.6, 0.0),
                FoodItem("chicken_leg_no_skin", "去皮鸡腿肉(生)", "g", "比带皮口感更好且低脂", 130.0, 20.0, 5.0, 0.0),
                FoodItem("beef_lean", "瘦牛肉(生)", "g", "如牛腱子/牛排，一手掌大约150g", 140.0, 22.0, 5.0, 0.0),
                FoodItem("beef_fatty", "肥牛/五花牛", "g", "火锅常用，脂肪较高", 330.0, 16.0, 30.0, 0.0),
                FoodItem("pork_lean", "瘦猪肉/里脊(生)", "g", "比肥肉健康", 143.0, 26.0, 4.0, 0.0),
                FoodItem("lamb_leg", "羊腿肉(生)", "g", "脂肪含量中等", 200.0, 25.0, 11.0, 0.0),
                FoodItem("egg_whole", "全鸡蛋", "个", "一个中等鸡蛋约50g", 72.0, 6.3, 5.0, 0.4),
                FoodItem("egg_white", "鸡蛋清", "个", "一个蛋清约35g，纯蛋白", 17.0, 3.6, 0.1, 0.2),
                FoodItem("shrimp", "虾仁(生)", "g", "极低脂高蛋白来源", 90.0, 18.0, 1.0, 0.0),
                FoodItem("salmon", "三文鱼(生)", "g", "富含健康Omega-3油脂", 208.0, 20.0, 13.0, 0.0),
                FoodItem("tuna_canned_water", "水浸金枪鱼罐头(沥干)", "g", "极方便即食蛋白质", 116.0, 26.0, 1.0, 0.0),
                FoodItem("fish_white", "白肉鱼类(如鳕鱼/龙利鱼)", "g", "低热量鱼类", 85.0, 19.0, 0.5, 0.0),
                FoodItem("tofu_firm", "老豆腐/北豆腐", "g", "植物蛋白，半块约200g", 100.0, 12.0, 5.0, 2.0),
                FoodItem("edamame", "毛豆(熟)", "g", "不错的植物蛋白零食", 122.0, 11.0, 5.0, 10.0),
                FoodItem("milk_whole", "全脂牛奶", "ml", "一盒通常250ml", 65.0, 3.2, 3.6, 4.8),
                FoodItem("milk_skim", "脱脂牛奶", "ml", "热量减半", 35.0, 3.4, 0.1, 5.0),
                FoodItem("soy_milk_unsweetened", "无糖豆浆", "ml", "植物蛋白饮料", 33.0, 3.0, 1.6, 1.6),
                FoodItem("greek_yogurt_plain", "原味希腊酸奶", "g", "质地浓稠，高蛋白", 60.0, 10.0, 0.4, 3.6),
                FoodItem("cottage_cheese_lowfat", "茅屋芝士(低脂)", "g", "健身常备，酪蛋白丰富", 72.0, 12.0, 1.0, 3.0),
                FoodItem("protein_bar_avg", "普通蛋白棒(估算)", "g", "品牌差异极大，仅供参考", 380.0, 30.0, 12.0, 35.0)
            )
        ),
        // ==================== 蔬菜 ====================
        FoodCategory(
            id = "veggies",
            name = "蔬菜与菌菇",
            items = listOf(
                // 大部分蔬菜热量极低，主要提供微量元素和纤维素
                FoodItem("broccoli", "西兰花", "g", "一小颗约200g", 34.0, 2.8, 0.4, 7.0),
                FoodItem("spinach", "菠菜", "g", "缩水严重，一大把煮完没多少", 23.0, 2.9, 0.4, 3.6),
                FoodItem("lettuce", "生菜/沙拉菜", "g", "热量极低，基本可忽略", 15.0, 1.4, 0.2, 2.9),
                FoodItem("cucumber", "黄瓜", "g", "一根约150-200g，水分大", 16.0, 0.7, 0.1, 3.6),
                FoodItem("tomato", "番茄", "g", "一个中等约150g", 18.0, 0.9, 0.2, 3.9),
                FoodItem("cherry_tomato", "圣女果/小番茄", "g", "一把约100g", 22.0, 1.1, 0.2, 4.8),
                FoodItem("carrot", "胡萝卜", "g", "一根中等约100g", 41.0, 0.9, 0.2, 9.6),
                FoodItem("bell_pepper", "甜椒/彩椒", "g", "一个大彩椒约150g", 20.0, 1.0, 0.2, 4.6),
                FoodItem("onion", "洋葱", "g", "一个中等洋葱约150g", 40.0, 1.1, 0.1, 9.3),
                FoodItem("asparagus", "芦笋", "g", "一捆约250g", 20.0, 2.2, 0.1, 3.9),
                FoodItem("mushroom", "蘑菇/香菇", "g", "各类菌菇平均值", 22.0, 3.0, 0.3, 3.0),
                FoodItem("cabbage", "卷心菜/包菜", "g", "便宜量大的蔬菜", 25.0, 1.3, 0.1, 5.8)
            )
        ),
        // ==================== 水果 ====================
        FoodCategory(
            id = "fruits",
            name = "水果",
            items = listOf(
                FoodItem("apple", "苹果", "g", "一个中等苹果约180g", 52.0, 0.3, 0.2, 14.0),
                FoodItem("banana", "香蕉", "g", "一根中等去皮约120g，适合训练前后", 89.0, 1.1, 0.3, 22.8),
                FoodItem("orange", "橙子", "g", "一个中等去皮约150g", 47.0, 0.9, 0.1, 11.8),
                FoodItem("blueberry", "蓝莓", "g", "一小盒约125g，富含抗氧化物", 57.0, 0.7, 0.3, 14.5),
                FoodItem("strawberry", "草莓", "g", "低热量水果，10颗大草莓约200g", 32.0, 0.7, 0.3, 7.7),
                FoodItem("grapes", "葡萄", "g", "含糖量较高", 69.0, 0.7, 0.2, 18.0),
                FoodItem("kiwi", "猕猴桃", "g", "一个约80g，维C丰富", 61.0, 1.1, 0.5, 14.7),
                FoodItem("watermelon", "西瓜", "g", "一片约300g，水分极大，热量低", 30.0, 0.6, 0.1, 7.6),
                FoodItem("avocado", "牛油果", "g", "高脂肪水果，半个约80g", 160.0, 2.0, 15.0, 9.0)
            )
        ),
        // ==================== 油脂与坚果 ====================
        FoodCategory(
            id = "fats",
            name = "油脂与坚果种子",
            items = listOf(
                // 油脂类热量密度极高，记录需谨慎
                FoodItem("olive_oil", "橄榄油/烹饪油", "g", "一平瓷勺约10g-12g，纯脂肪", 884.0, 0.0, 100.0, 0.0),
                FoodItem("butter", "黄油", "g", "一小块约10g", 717.0, 0.8, 81.0, 0.1),
                FoodItem("peanut_butter", "花生酱(无糖)", "g", "一勺约20g，高脂高蛋白", 588.0, 25.0, 50.0, 20.0),
                FoodItem("almonds", "巴旦木/杏仁", "g", "一小把约25g(约20颗)", 579.0, 21.0, 49.0, 21.0),
                FoodItem("walnut", "核桃仁", "g", "几个核桃约20g，优质脂肪", 654.0, 15.0, 65.0, 14.0),
                FoodItem("chia_seeds", "奇亚籽", "g", "常泡酸奶，高纤维高脂", 486.0, 16.5, 30.7, 42.0),
                FoodItem("dark_chocolate_85", "85%黑巧克力", "g", "一小排约20g", 530.0, 9.0, 45.0, 20.0)
            )
        ),
        // ==================== 新增：饮料与调味 ====================
        FoodCategory(
            id = "drinks_misc",
            name = "饮料与调味品",
            items = listOf(
                FoodItem("cola_regular", "可乐(含糖)", "ml", "一听330ml含糖约35g", 42.0, 0.0, 0.0, 10.6),
                FoodItem("cola_zero", "零度可乐/无糖饮料", "ml", "代糖，热量基本为0", 0.0, 0.0, 0.0, 0.0),
                FoodItem("orange_juice_100", "100%橙汁", "ml", "含天然果糖，也要适量", 45.0, 0.7, 0.2, 10.0),
                FoodItem("coffee_black", "纯黑咖啡/美式", "ml", "热量极低", 2.0, 0.3, 0.0, 0.2),
                FoodItem("latte_whole_milk", "拿铁(全脂奶)", "ml", "主要热量来自牛奶", 55.0, 3.0, 3.0, 4.0),
                FoodItem("soy_sauce", "酱油/生抽", "ml", "高钠，热量较低", 60.0, 8.0, 0.0, 6.0),
                FoodItem("mayonnaise", "蛋黄酱/美乃滋", "g", "热量炸弹，一勺约15g", 680.0, 1.0, 75.0, 1.0),
                FoodItem("ketchup", "番茄酱", "g", "含糖量不低，一勺约15g", 100.0, 1.5, 0.2, 25.0)
            )
        )
    )
}