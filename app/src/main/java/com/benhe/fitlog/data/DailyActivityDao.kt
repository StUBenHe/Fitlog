package com.benhe.fitlog.data

import androidx.room.*
import com.benhe.fitlog.model.DailyActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyActivityDao {
    // 获取单日数据 (用于详情页)
    @Query("SELECT * FROM daily_activity WHERE date = :date")
    fun getActivityByDate(date: String): Flow<DailyActivity?>

    // 插入或更新
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateActivity(activity: DailyActivity)

    // ✅ 新增：获取所有日期的记录 (用于点亮日历上的圆环)
    @Query("SELECT * FROM daily_activity")
    fun getAllActivities(): Flow<List<DailyActivity>>
}