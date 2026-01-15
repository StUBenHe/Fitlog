package com.benhe.fitlog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.benhe.fitlog.data.local.dao.BodyStatDao
import com.benhe.fitlog.data.local.dao.DailyActivityDao
import com.benhe.fitlog.data.local.dao.DietDao
import com.benhe.fitlog.data.local.dao.WorkoutDao
import com.benhe.fitlog.data.local.entiy.DietRecord
import com.benhe.fitlog.data.local.entiy.ExerciseCatalog
import com.benhe.fitlog.data.local.entiy.WorkoutSession
import com.benhe.fitlog.data.local.entiy.WorkoutSet
import com.benhe.fitlog.model.BodyStatRecord
import com.benhe.fitlog.model.DailyActivity

/**
 * 应用的核心 Room 数据库定义类。
 *
 * 这个类定义了数据库包含的所有表（Entities）、使用的类型转换器（TypeConverters）以及数据库的版本号。
 * 它采用单例模式实现，确保在整个应用生命周期中只存在一个数据库实例连接。
 *
 * @property bodyStatDao 提供对身体成分数据的访问接口。
 * @property dietDao 提供对饮食记录数据的访问接口。
 * @property dailyActivityDao 提供对每日活动数据的访问接口。
 * @property workoutDao 提供对训练相关数据（动作库、训练会话、训练组）的访问接口。
 */
@Database(
    // 定义数据库中包含的所有数据表对应的实体类
    entities = [
        DietRecord::class,     // 饮食记录表
        DailyActivity::class,  // 每日活动统计表
        ExerciseCatalog::class,// 动作库表
        WorkoutSession::class, // 训练会话表
        WorkoutSet::class,     // 训练组详情表
        BodyStatRecord::class  // 身体成分记录表
    ],
    // 数据库版本号。当实体类的结构发生变化时，需要增加版本号并提供迁移策略。
    version = 12,
    // 是否将数据库的 schema 导出到文件中，用于版本控制和检查。设置为 false 表示不导出。
    exportSchema = false
)
// 指定用于处理复杂数据类型（如 LocalDate, Map 等）的类型转换器类。
@TypeConverters(FitlogConverters::class)
abstract class AppDatabase : RoomDatabase() {

    // --- DAO 获取方法定义 (由 Room 自动生成实现) ---
    abstract fun bodyStatDao(): BodyStatDao
    abstract fun dietDao(): DietDao
    abstract fun dailyActivityDao(): DailyActivityDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        // 使用 @Volatile 确保 INSTANCE 变量在多线程环境下的可见性，防止出现指令重排导致的问题。
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * 获取数据库实例的线程安全方法。
         * 采用了双重检查锁定 (Double-Checked Locking) 模式来实现单例。
         *
         * @param context 应用上下文，用于创建数据库构建器。建议传入 applicationContext。
         * @return 全局唯一的 AppDatabase 实例。
         */
        fun getDatabase(context: Context): AppDatabase {
            // 第一次检查，如果实例已存在则直接返回，避免不必要的同步块开销。
            return INSTANCE ?: synchronized(this) {
                // 第二次检查，进入同步块后再次确认，防止多个线程同时通过了第一次检查。
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitlog_database" // 数据库在设备存储中的文件名
                )
                    // 【重要开发配置】
                    // 允许破坏性迁移。当数据库版本升级且没有提供对应的 Migration 策略时，Room 会清空所有数据并重建表结构。
                    // 这在开发阶段非常方便，但在生产环境中应慎用，应提供具体的迁移脚本。
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}