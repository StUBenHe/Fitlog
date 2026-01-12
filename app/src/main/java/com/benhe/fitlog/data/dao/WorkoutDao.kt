package com.benhe.fitlog.data.dao



import androidx.room.*
import com.benhe.fitlog.data.entity.ExerciseCatalog
import com.benhe.fitlog.data.entity.WorkoutSession
import com.benhe.fitlog.data.entity.WorkoutSet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WorkoutDao {
    // --- 动作库 ---
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