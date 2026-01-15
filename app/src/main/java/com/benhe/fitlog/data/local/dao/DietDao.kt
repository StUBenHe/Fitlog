package com.benhe.fitlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.benhe.fitlog.data.local.entiy.DietRecord
import kotlinx.coroutines.flow.Flow

/**
 * 饮食记录数据的访问接口 (Data Access Object)。
 * 负责对 diet_records 表进行操作，处理具体的每一餐食物记录。
 */
@Dao
interface DietDao {
    /**
     * 插入一条新的饮食记录。
     *
     * @param record 要插入的饮食记录实体对象。
     */
    @Insert
    suspend fun insertRecord(record: DietRecord)

    /**
     * 获取指定日期的所有饮食记录列表。
     * 记录按时间戳降序排列（最近吃的排在前面），用于在饮食页面展示当天的用餐明细。
     *
     * @param date 查询日期字符串。
     * @return 发出当天所有饮食记录列表的 Flow。
     */
    @Query("SELECT * FROM diet_records WHERE date = :date ORDER BY timestamp DESC")
    fun getRecordsByDate(date: String): Flow<List<DietRecord>>

    /**
     * 计算指定日期摄入的总卡路里。
     * 这是一个聚合查询，直接在数据库层面完成加和计算。
     *
     * @param date 查询日期字符串。
     * @return 发出当天总卡路里数值的 Flow，如果当天无记录则可能为 null。
     */
    @Query("SELECT SUM(calories) FROM diet_records WHERE date = :date")
    fun getTotalCaloriesByDate(date: String): Flow<Double?>

    /**
     * 计算指定日期摄入的总蛋白质含量。
     *
     * @param date 查询日期字符串。
     * @return 发出当天总蛋白质数值的 Flow。
     */
    @Query("SELECT SUM(protein) FROM diet_records WHERE date = :date")
    fun getTotalProteinForDate(date: String): Flow<Double?>

    /**
     * 计算指定日期摄入的总碳水化合物含量。
     *
     * @param date 查询日期字符串。
     * @return 发出当天总碳水数值的 Flow。
     */
    @Query("SELECT SUM(carbs) FROM diet_records WHERE date = :date")
    fun getTotalCarbsByDate(date: String): Flow<Double?>

    /**
     * 删除一条指定的饮食记录。
     *
     * @param record 要删除的饮食记录实体对象（Room 会根据主键匹配要删除的行）。
     */
    @Delete
    suspend fun deleteRecord(record: DietRecord)
}