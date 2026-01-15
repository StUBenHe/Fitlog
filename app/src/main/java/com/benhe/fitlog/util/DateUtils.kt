package com.benhe.fitlog.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    // 将日期格式化为 2023-10-27 这种格式，作为数据库的 Key
    val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // 获取当天的周几，例如 "周五"
    fun getWeekday(date: LocalDate): String {
        return  date.format(DateTimeFormatter.ofPattern("E", Locale.CHINA))
    }

    // 生成一组日期列表（比如今天前后的各15天，共31天）
    fun getCalendarRange(): List<LocalDate> {
        val today = LocalDate.now()
        val list = mutableListOf<LocalDate>()
        for (i in -15..15) {
            list.add(today.plusDays(i.toLong()))
        }
        return list
    }
}