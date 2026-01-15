package com.benhe.fitlog.data.local.entiy

import androidx.room.*
import com.benhe.fitlog.model.BodyRegion
import java.time.LocalDate

/**
 * ==========================
 * 训练相关数据库实体定义文件
 * ==========================
 * 这个文件包含了与训练记录相关的三个核心数据表定义。
 * 它们共同构成了训练模块的数据存储结构。
 */

/**
 * **1. 动作字典表 (Exercise Catalog)**
 *
 * 这是一张基础信息表，存储了系统中所有可供选择的训练动作定义。
 * 它类似于一本字典，定义了每个动作的名称、类别以及它主要锻炼的身体部位和相应的权重。
 *
 * @property exerciseId 动作的唯一标识符（主键）。通常使用易读的字符串，如 "barbell_bench_press"。
 * @property name 动作的显示名称，如 "杠铃卧推"。
 * @property category 动作类别，例如：力量 (Strength), 有氧 (Cardio), 拉伸 (Stretching)。
 * @property regionWeights 关键字段。定义该动作对身体各部位的影响权重 (0.0 - 1.0)。
 *                         这是一个 Map 类型，Room 会使用 TypeConverter 将其转换为字符串存储。
 *                         存储格式示例: "CHEST:1.0,SHOULDERS:0.3" 表示主要练胸，稍微带到一点肩膀。
 */
@Entity(tableName = "exercise_catalog")
data class ExerciseCatalog(
    @PrimaryKey val exerciseId: String,
    val name: String,
    val category: String,
    val regionWeights: Map<BodyRegion, Float>
)

/**
 * **2. 训练会话表 (Workout Session)**
 *
 * 这是一张记录用户单次完整训练的主表（Header Table）。通常一次训练对应一天。
 * 它记录了这次训练的总体信息，如日期、时间、总容量等。
 * 具体的训练动作组细节存储在下面的 `WorkoutSet` 表中，通过 `sessionId` 进行关联。
 *
 * @property sessionId 会话的唯一 ID（主键）。由数据库自动生成自增的 Long 类型整数。
 * @property date 训练发生的日期。用于与首页日历等功能进行关联。Room 会使用 TypeConverter 存储为字符串。
 * @property startTime 训练开始的时间戳（毫秒）。
 * @property endTime 训练结束的时间戳（毫秒）。如果不为空，可用于计算训练时长。
 * @property totalVolume 预计算的本次训练总容量（总负荷 = 所有组的 重量 * 次数 之和）。方便快速查询和统计。
 * @property note 用户对本次训练的总体备注或心得。
 */
@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val sessionId: Long = 0,
    val date: LocalDate,
    val startTime: Long,
    val endTime: Long? = null,
    val totalVolume: Float = 0f,
    val note: String? = null
)

/**
 * **3. 训练组表 (Workout Set)**
 *
 * 这是一张记录具体训练动作的明细表（Detail Table）。
 * 它记录了用户在一次训练中做的每一组动作的具体数据。它是训练数据中最细粒度的记录。
 *
 * **关联关系：** 通过外键 `sessionId` 与 `WorkoutSession` 表进行多对一关联（一次会话包含多个训练组）。
 * **关联关系：** 通过外键 `exerciseId` 与 `ExerciseCatalog` 表关联，明确这一组做的是什么动作。
 *
 * @property setId 训练组的唯一 ID（主键）。由数据库自动生成。
 * @property sessionId 外键。关联到所属的训练会话 ID。
 * @property region 冗余字段。记录该组动作主要锻炼的部位。方便进行部位维度的快速统计分析，而无需每次都去查动作库表。
 * @property exerciseId 外键。关联到具体的动作 ID，表明这一组做的是哪个动作。
 * @property weight 本组使用的重量（单位需统一，如 kg）。
 * @property reps 本组完成的次数 (Repetitions)。
 * @property rpe (Optional) 自觉运动强度评分 (Rating of Perceived Exertion)，通常为 1-10 分。
 * @property note (Optional) 针对这一特定组的备注。
 * @property timestamp 本组完成时的具体时间戳。默认值为当前时间。
 */
@Entity(
    tableName = "workout_sets",
    // 定义外键约束，确保数据的完整性。
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSession::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            // 当主表(WorkoutSession)中的记录被删除时，级联删除子表(WorkoutSet)中所有关联的记录。
            onDelete = ForeignKey.CASCADE
        ),
        // (可选，但推荐) 也可以为 exerciseId 添加外键约束，指向 ExerciseCatalog
        /*
        ForeignKey(
            entity = ExerciseCatalog::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.NO_ACTION // 删除动作库动作时，保留历史记录
        )
        */
    ],
    // 为 sessionId 创建索引，提高查询特定会话下所有组的速度。
    indices = [Index(value = ["sessionId"])]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true) val setId: Long = 0,
    val sessionId: Long,
    val region: BodyRegion,
    val exerciseId: String,
    val weight: Float,
    val reps: Int,
    val rpe: Int? = null,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)