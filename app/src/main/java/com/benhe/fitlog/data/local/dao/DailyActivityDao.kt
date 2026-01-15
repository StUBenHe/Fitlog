package com.benhe.fitlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benhe.fitlog.model.DailyActivity
import kotlinx.coroutines.flow.Flow

/**
 * 每日活动统计数据的访问接口 (Data Access Object)。
 * 负责对 daily_activity 表进行操作，存储每日的步数、卡路里消耗等汇总信息。
 */
@Dao
interface DailyActivityDao {
    /**
     * 根据指定日期获取当天的活动数据。
     * 用于在主页或详情页显示某一天的具体表现。
     *
     * @param date 日期字符串，格式需与数据库存储格式一致 (例如 "YYYY-MM-DD")。
     * @return 发出当天活动记录的 Flow，如果没有当天的记录则发出 null。
     */
    @Query("SELECT * FROM daily_activity WHERE date = :date")
    fun getActivityByDate(date: String): Flow<DailyActivity?>

    /**
     * 插入新的活动记录，或更新已存在的记录。
     * 如果数据库中已经有了当天的记录（根据主键判断），则用新数据覆盖它。
     *
     * @param activity 要保存的每日活动实体对象。
     */
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrUpdateActivity(activity: DailyActivity)

    /**
     * 获取数据库中存储的所有日期的活动记录。
     * 通常用于在日历视图上标记出哪些天有活动记录（例如点亮日历上的圆环）。
     *
     * @return 发出包含所有历史活动记录列表的 Flow。
     */
    @Query("SELECT * FROM daily_activity")
    fun getAllActivities(): Flow<List<DailyActivity>>
}