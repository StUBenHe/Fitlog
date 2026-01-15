package com.benhe.fitlog.data.local.dao

import androidx.room.*
import com.benhe.fitlog.data.local.entiy.ExerciseCatalog
import com.benhe.fitlog.data.local.entiy.WorkoutSession
import com.benhe.fitlog.data.local.entiy.WorkoutSet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * 训练相关数据的访问接口 (Data Access Object)。
 * 这是一个复杂的 DAO，同时管理动作库 (Catalog)、训练会话 (Session) 和具体训练组 (Set) 三张表的数据。
 */
@Dao
interface WorkoutDao {

    // --- 动作库相关操作 (Exercise Catalog) ---

    /**
     * 获取所有的训练组记录，按时间戳降序排列。
     * @return 发出所有训练组列表的 Flow。
     */
    @Query("SELECT * FROM workout_sets ORDER BY timestamp DESC")
    fun getAllSets(): Flow<List<WorkoutSet>>

    /**
     * 获取指定时间戳范围内的所有训练组记录。
     *
     * @param startDate 起始时间戳（毫秒）。
     * @param endDate 结束时间戳（毫秒）。
     * @return 发出符合条件的训练组列表的 Flow。
     */
    @Query("SELECT * FROM workout_sets WHERE timestamp >= :startDate AND timestamp <= :endDate")
    fun getSetsInDateRange(startDate: Long, endDate: Long): Flow<List<WorkoutSet>>

    /**
     * 批量插入动作库数据。
     * 如果遇到冲突（例如动作 ID 相同），则替换旧数据。通常用于初始化或更新预置的动作列表。
     *
     * @param exercises 要插入的动作列表。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseCatalog>)

    /**
     * 获取动作库中的所有动作定义。
     * 用于在添加训练动作时供用户选择。
     *
     * @return 发出所有可用动作列表的 Flow。
     */
    @Query("SELECT * FROM exercise_catalog")
    fun getAllExercises(): Flow<List<ExerciseCatalog>>


    // --- 训练记录相关操作 (Session & Sets) ---

    /**
     * 插入一次新的训练会话（一次完整的训练）。
     *
     * @param session 训练会话实体对象。
     * @return 返回新插入行的主键 ID (sessionId)。
     */
    @Insert
    suspend fun insertSession(session: WorkoutSession): Long

    /**
     * 插入一组具体的训练记录（例如：卧推 100kg 做 8 次）。
     *
     * @param workoutSet 训练组实体对象。
     */
    @Insert
    suspend fun insertSet(workoutSet: WorkoutSet)

    /**
     * 根据日期查询当天的训练会话信息。
     *
     * @param date 查询日期 (LocalDate 类型，Room 会自动转换)。
     * @return 发出当天的训练会话 Flow，如果没有则为 null。
     */
    @Query("SELECT * FROM workout_sessions WHERE date = :date")
    fun getSessionByDate(date: LocalDate): Flow<WorkoutSession?>

    /**
     * 这是一个事务查询，用于获取指定日期范围内的所有训练组。
     * 通过内连接 (INNER JOIN) 将 workout_sets 表和 workout_sessions 表关联起来，
     * 以便根据 session 的日期来筛选 set。
     *
     * @param startDate 起始日期。
     * @param endDate 结束日期。
     * @return 发出符合日期范围的训练组列表的 Flow。
     */
    @Transaction
    @Query("""
        SELECT s.* FROM workout_sets s 
        INNER JOIN workout_sessions sess ON s.sessionId = sess.sessionId 
        WHERE sess.date BETWEEN :startDate AND :endDate
    """)
    fun getSetsInDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<WorkoutSet>>

    /**
     * 获取特定训练会话 (Session) 下属的所有训练组。
     *
     * @param sessionId 训练会话的 ID。
     * @return 发出该会话下所有训练组列表的 Flow。
     */
    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId")
    fun getSetsBySession(sessionId: Long): Flow<List<WorkoutSet>>

    /**
     * 删除指定时间戳范围内的所有训练组。
     * 通常用于按天清除数据。
     *
     * @param dayStart 当天的起始时间戳。
     * @param dayEnd 当天的结束时间戳。
     */
    @Query("DELETE FROM workout_sets WHERE timestamp >= :dayStart AND timestamp <= :dayEnd")
    suspend fun deleteSetsByDay(dayStart: Long, dayEnd: Long)

    /**
     * 获取指定时间戳范围内的所有训练组。
     * (功能与上面的 getSetsInDateRange(Long, Long) 类似，可能是不同场景下的复用)
     *
     * @param start 起始时间戳。
     * @param end 结束时间戳。
     * @return 发出符合条件的训练组列表的 Flow。
     */
    @Query("SELECT * FROM workout_sets WHERE timestamp >= :start AND timestamp <= :end")
    fun getSetsByTimeRange(start: Long, end: Long): Flow<List<WorkoutSet>>

    /**
     * 删除指定时间戳范围内的所有训练组。
     * 用于“覆盖保存”逻辑，在保存新数据前先清除旧时段的数据，避免重复。
     *
     * @param start 起始时间戳。
     * @param end 结束时间戳。
     */
    @Query("DELETE FROM workout_sets WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun deleteSetsByTimeRange(start: Long, end: Long)
}