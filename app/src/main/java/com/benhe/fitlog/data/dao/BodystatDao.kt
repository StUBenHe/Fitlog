package com.benhe.fitlog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benhe.fitlog.model.BodyStatRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyStatDao {
    // 获取最新的那条数据（用于显示个人档案）
    // 注意：表名必须和 BodyStatRecord 里的 tableName 一致
    @Query("SELECT * FROM body_stat_history ORDER BY timestamp DESC LIMIT 1")
    fun getLatestStat(): Flow<BodyStatRecord?>

    // 获取历史数据（用于画折线图）
    @Query("SELECT * FROM body_stat_history ORDER BY timestamp ASC")
    fun getHistoryStats(): Flow<List<BodyStatRecord>>

    // 插入或替换数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStat(stat: BodyStatRecord)
}