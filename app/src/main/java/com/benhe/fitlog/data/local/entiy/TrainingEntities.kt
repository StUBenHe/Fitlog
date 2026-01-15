package com.benhe.fitlog.data.local.entiy

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


@Entity(tableName = "workout_sets")
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true) val setId: Long = 0,
    val sessionId: Long,
    val region: BodyRegion,
    val exerciseId: String,
    val weight: Float,
    val reps: Int,
    val rpe: Int? = null,
    val note: String? = null, // 新增字段
    val timestamp: Long = System.currentTimeMillis()
)