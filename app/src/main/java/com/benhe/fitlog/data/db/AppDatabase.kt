package com.benhe.fitlog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
// 导入 DAO
import com.benhe.fitlog.data.DailyActivityDao
import com.benhe.fitlog.data.dao.WorkoutDao
// 导入实体类 (根据你的截图，它们在 entity 文件夹的 TrainingEntities.kt 里)
import com.benhe.fitlog.data.entity.ExerciseCatalog
import com.benhe.fitlog.data.entity.WorkoutSession
import com.benhe.fitlog.data.entity.WorkoutSet
// 导入模型和转换器
import com.benhe.fitlog.model.DailyActivity
import com.benhe.fitlog.data.converters.FitlogConverters

import com.benhe.fitlog.model.BodyStatRecord // 确保导入这个
import com.benhe.fitlog.data.dao.BodyStatDao



@Database(
    entities = [
        DietRecord::class,
        DailyActivity::class,
        ExerciseCatalog::class,
        WorkoutSession::class,
        WorkoutSet::class,
        BodyStatRecord::class
    ],
    version = 12,
    exportSchema = false
)
@TypeConverters(FitlogConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bodyStatDao(): BodyStatDao
    abstract fun dietDao(): DietDao
    abstract fun dailyActivityDao(): DailyActivityDao
    abstract fun workoutDao(): WorkoutDao // 对应 com.benhe.fitlog.data.dao.WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitlog_database"
                )
                    // 允许破坏性迁移（开发阶段如果数据库结构变了，直接清空重建，防止崩溃）
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}


