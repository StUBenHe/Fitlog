package com.benhe.fitlog.data.entity

import androidx.room.*
import com.benhe.fitlog.model.BodyRegion
import java.time.LocalDate

/**
 * 1. 动作字典表：定义每个动作对 8 大区块的影响权重
 */
@Entity(tableName = "exercise_catalog")
data class ExerciseCatalog(
    @PrimaryKey val exerciseId: String, // 如 "barbell_bench_press"
    val name: String,
    val category: String, // 力量, 有氧, 拉伸
    // 关键：该动作关联的区块及其影响系数 (0.0 - 1.0)
    // 存储格式示例: "CHEST:1.0,SHOULDERS:0.3"
    val regionWeights: Map<BodyRegion, Float>
)

/**
 * 2. 训练会话表：记录用户的一次训练（通常对应一天）
 */
@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val sessionId: Long = 0,
    val date: LocalDate, // 关联首页日历
    val startTime: Long,
    val endTime: Long? = null,
    val totalVolume: Float = 0f, // 预计算的总容量
    val note: String? = null
)

/**
 * 3. 组数记录表：每一组的具体重量和次数
 */
@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId"), Index("exerciseId")]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true) val setId: Long = 0,
    val sessionId: Long,
    val exerciseId: String, // 关联动作库
    val weight: Float,      // 公斤
    val reps: Int,          // 次数
    val rpe: Int? = null,   // 自感用力程度 (1-10)
    val isWarmup: Boolean = false, // 是否为热身组（计算负荷时可剔除）
    val timestamp: Long = System.currentTimeMillis()
){
    // 添加这个函数，方便 LoadCalculator 调用
    fun getLocalDate(): java.time.LocalDate {
        return java.time.Instant.ofEpochMilli(timestamp)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
    }
}
