package com.benhe.fitlog.data

import androidx.room.*
import com.benhe.fitlog.model.DailyActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyActivityDao {
    @Query("SELECT * FROM daily_activity WHERE date = :date")
    fun getActivityByDate(date: String): Flow<DailyActivity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateActivity(activity: DailyActivity)
}