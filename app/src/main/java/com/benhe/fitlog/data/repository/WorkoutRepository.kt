package com.benhe.fitlog.data.repository

import com.benhe.fitlog.data.dao.WorkoutDao
import com.benhe.fitlog.data.entity.WorkoutSet
import com.benhe.fitlog.model.BodyRegion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class WorkoutRepository(private val dao: WorkoutDao) {

    fun getRecentSetsFlow(): Flow<List<WorkoutSet>> {
        // 只需要开始时间（14天前）
        val startMillis = LocalDate.now().minusDays(14)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val futureMillis = System.currentTimeMillis() + 86400000L // 加上一天

        return dao.getSetsInDateRange(startMillis, futureMillis)
    }
}