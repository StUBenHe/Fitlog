package com.benhe.fitlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.benhe.fitlog.data.local.entiy.DietRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface DietDao {
    @Insert
    suspend fun insertRecord(record: DietRecord)

    @Query("SELECT * FROM diet_records WHERE date = :date ORDER BY timestamp DESC")
    fun getRecordsByDate(date: String): Flow<List<DietRecord>>

    @Query("SELECT SUM(calories) FROM diet_records WHERE date = :date")
    fun getTotalCaloriesByDate(date: String): Flow<Double?>

    @Query("SELECT SUM(protein) FROM diet_records WHERE date = :date")
    fun getTotalProteinForDate(date: String): Flow<Double?>

    @Query("SELECT SUM(carbs) FROM diet_records WHERE date = :date")
    fun getTotalCarbsByDate(date: String): Flow<Double?>

    @Delete
    suspend fun deleteRecord(record: DietRecord)

}