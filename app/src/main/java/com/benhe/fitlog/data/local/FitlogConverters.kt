package com.benhe.fitlog.data.local

import androidx.room.TypeConverter
import com.benhe.fitlog.model.BodyRegion
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Room 数据库类型转换器类。
 *
 * Room 默认只能存储基本数据类型（如 Int, String, Long 等）。
 * 对于复杂的数据类型（如日期对象 LocalDate、集合对象 Map 等），需要提供转换器告诉 Room 如何将它们与基本类型进行互转。
 * 通过在 AppDatabase 上使用 @TypeConverters 注解来注册此类。
 */
class FitlogConverters {
    // 使用 ISO-8601 标准的日期格式 (YYYY-MM-DD) 进行转换，确保格式统一。
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    // --- LocalDate 与 String 的转换 ---

    /**
     * 将 LocalDate 对象转换为符合 ISO-8601 格式的字符串存入数据库。
     * 例如: 2023-10-27
     */
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.format(formatter)

    /**
     * 将数据库中读取的 ISO-8601 格式字符串还原为 LocalDate 对象。
     */
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it, formatter) }


    // --- Map<BodyRegion, Float> 与 String 的转换 ---
    // 用于存储身体各部位的训练负荷比例等数据。

    /**
     * 将 Map<BodyRegion, Float> 集合转换为一个逗号分隔的自定义格式字符串存入数据库。
     * 格式示例: "CHEST:0.5,BACK:0.3,LEGS:0.2"
     *
     * @param value 要转换的 Map 集合。
     * @return 转换后的字符串。
     */
    @TypeConverter
    fun fromRegionMap(value: Map<BodyRegion, Float>): String {
        // 使用 joinToString 将每个键值对拼接成 "KEY:VALUE" 的形式，再用逗号连接。
        return value.entries.joinToString(",") { "${it.key.name}:${it.value}" }
    }

    /**
     * 将数据库中读取的自定义格式字符串还原为 Map<BodyRegion, Float> 集合。
     *
     * @param value 从数据库读取的字符串。
     * @return 解析后的 Map 集合。如果解析失败或字符串为空，则返回空 Map。
     */
    @TypeConverter
    fun toRegionMap(value: String): Map<BodyRegion, Float> {
        if (value.isBlank()) return emptyMap()
        return try {
            // 先按逗号分割成一个个 "KEY:VALUE" 的组合
            value.split(",").associate {
                // 再按冒号分割出键和值
                val parts = it.split(":")
                // 将字符串还原为枚举类型和浮点数类型
                BodyRegion.valueOf(parts[0]) to parts[1].toFloat()
            }
        } catch (e: Exception) {
            // 捕获解析过程中可能出现的异常（如枚举名对不上、数字格式错误），返回空 Map 防止崩溃。
            // 在实际开发中，这里也可以考虑记录错误日志。
            emptyMap()
        }
    }
}