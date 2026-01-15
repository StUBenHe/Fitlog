package com.benhe.fitlog.data.local

import androidx.room.TypeConverter
import com.benhe.fitlog.model.BodyRegion
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FitlogConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.format(formatter)

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it, formatter) }

    @TypeConverter
    fun fromRegionMap(value: Map<BodyRegion, Float>): String {
        return value.entries.joinToString(",") { "${it.key.name}:${it.value}" }
    }

    @TypeConverter
    fun toRegionMap(value: String): Map<BodyRegion, Float> {
        if (value.isBlank()) return emptyMap()
        return try {
            value.split(",").associate {
                val parts = it.split(":")
                BodyRegion.valueOf(parts[0]) to parts[1].toFloat()
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}