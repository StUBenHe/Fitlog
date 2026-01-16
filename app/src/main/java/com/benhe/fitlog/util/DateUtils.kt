package com.benhe.fitlog.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    // 获取当天的周几，例如 "周五"
    fun getWeekday(date: LocalDate): String {
        return  date.format(DateTimeFormatter.ofPattern("E", Locale.CHINA))
    }

}