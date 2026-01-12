package com.benhe.fitlog.data.repository

import com.benhe.fitlog.data.dao.WorkoutDao
import com.benhe.fitlog.data.entity.WorkoutSet
import com.benhe.fitlog.logic.LoadCalculator
import com.benhe.fitlog.model.BodyRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

class WorkoutRepository(private val dao: WorkoutDao) {

    fun getBodyStatusFlow(): Flow<Map<BodyRegion, Float>> {
        // 转换日期为 Long 毫秒值，以匹配 DAO 的查询参数
        val startMillis = LocalDate.now().minusDays(14)
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = System.currentTimeMillis()

        // 这里的函数名 getSetsInDateRange 也要确保在 Dao 中存在
        return dao.getSetsInDateRange(startMillis, endMillis).map { sets ->
            LoadCalculator.calculateRegionStatus(
                sets = sets,
                sleepHours = 8f,
                proteinGrams = 100f
            )
        }
    }


    val allSets: Flow<List<WorkoutSet>> = dao.getAllSets()
}