package com.benhe.fitlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benhe.fitlog.model.BodyStatRecord
import kotlinx.coroutines.flow.Flow

/**
 * 身体成分数据的访问接口 (Data Access Object)。
 * 负责对 body_stat_history 表进行增删改查操作。
 */
@Dao
interface BodyStatDao {
    /**
     * 获取最新的那条身体成分数据。
     * 通常用于在个人档案页面显示当前的状态（如最新体重、体脂率）。
     *
     * @return 发出最近一条记录的 Flow，如果表为空则发出 null。
     */
    @Query("SELECT * FROM body_stat_history ORDER BY timestamp DESC LIMIT 1")
    fun getLatestStat(): Flow<BodyStatRecord?>

    /**
     * 获取所有的历史身体成分数据，按时间正序排列。
     * 通常用于绘制身体变化趋势的折线图。
     *
     * @return 发出包含所有历史记录列表的 Flow。
     */
    @Query("SELECT * FROM body_stat_history ORDER BY timestamp ASC")
    fun getHistoryStats(): Flow<List<BodyStatRecord>>

    /**
     * 插入一条新的身体成分记录，或者替换已存在的记录。
     * 使用 REPLACE 策略，如果新记录的主键与旧记录冲突，则覆盖旧记录。
     *
     * @param stat 要插入或更新的身体成分记录实体对象。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStat(stat: BodyStatRecord)
}