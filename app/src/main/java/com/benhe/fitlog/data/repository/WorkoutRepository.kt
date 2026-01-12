package com.benhe.fitlog.data.repository

import com.benhe.fitlog.data.dao.WorkoutDao
import com.benhe.fitlog.logic.LoadCalculator
import com.benhe.fitlog.model.BodyRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate

class WorkoutRepository(private val dao: WorkoutDao) {
    fun getBodyStatusFlow(): Flow<Map<BodyRegion, Float>> {
        val last7Days = LocalDate.now().minusDays(7)
        return combine(
            dao.getSetsInDateRange(last7Days, LocalDate.now()),
            dao.getAllExercises()
        ) { sets, exercises ->
            val catalogMap = exercises.associateBy { it.exerciseId }
            LoadCalculator.calculateRegionLoads(sets, catalogMap)
        }
    }
}