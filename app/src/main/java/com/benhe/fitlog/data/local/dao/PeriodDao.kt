package com.benhe.fitlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import com.benhe.fitlog.model.PeriodDay
@Dao
interface PeriodDao {
    // 获取所有记录的经期日期，按时间倒序排列（方便计算）
    @Query("SELECT * FROM period_days ORDER BY date DESC")
    fun getAllPeriodDays(): Flow<List<PeriodDay>>

    // 查询某一天是否是经期
    @Query("SELECT EXISTS(SELECT 1 FROM period_days WHERE date = :date)")
    suspend fun isPeriodDay(date: LocalDate): Boolean

    // 插入或替换（用于标记）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(periodDay: PeriodDay)

    // 删除（用于取消标记）
    @Delete
    suspend fun delete(periodDay: PeriodDay)
}