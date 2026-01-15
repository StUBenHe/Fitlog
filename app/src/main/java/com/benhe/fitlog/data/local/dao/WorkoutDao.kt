package com.benhe.fitlog.data.local.dao



import androidx.room.*
import com.benhe.fitlog.data.local.entiy.ExerciseCatalog
import com.benhe.fitlog.data.local.entiy.WorkoutSession
import com.benhe.fitlog.data.local.entiy.WorkoutSet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WorkoutDao {


    // --- 动作库 ---

    @Query("SELECT * FROM workout_sets ORDER BY timestamp DESC")
    fun getAllSets(): Flow<List<WorkoutSet>> // <--- 确认这里叫 getAllSets

    @Query("SELECT * FROM workout_sets WHERE timestamp >= :startDate AND timestamp <= :endDate")
    fun getSetsInDateRange(startDate: Long, endDate: Long): Flow<List<WorkoutSet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseCatalog>)

    @Query("SELECT * FROM exercise_catalog")
    fun getAllExercises(): Flow<List<ExerciseCatalog>>

    // --- 训练记录 ---
    @Insert
    suspend fun insertSession(session: WorkoutSession): Long

    @Insert
    suspend fun insertSet(workoutSet: WorkoutSet)

    @Query("SELECT * FROM workout_sessions WHERE date = :date")
    fun getSessionByDate(date: LocalDate): Flow<WorkoutSession?>

    @Transaction
    @Query("""
        SELECT s.* FROM workout_sets s 
        INNER JOIN workout_sessions sess ON s.sessionId = sess.sessionId 
        WHERE sess.date BETWEEN :startDate AND :endDate
    """)
    fun getSetsInDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<WorkoutSet>>


    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId")
    fun getSetsBySession(sessionId: Long): Flow<List<WorkoutSet>>

    @Query("DELETE FROM workout_sets WHERE timestamp >= :dayStart AND timestamp <= :dayEnd")
    suspend fun deleteSetsByDay(dayStart: Long, dayEnd: Long)
    @Query("SELECT * FROM workout_sets WHERE timestamp >= :start AND timestamp <= :end")
    fun getSetsByTimeRange(start: Long, end: Long): Flow<List<WorkoutSet>>

    // 顺便确保你有这个删除方法，用于“覆盖保存”逻辑
    @Query("DELETE FROM workout_sets WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun deleteSetsByTimeRange(start: Long, end: Long)


}