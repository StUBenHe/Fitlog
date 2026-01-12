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

@Database(
    entities = [
        DietRecord::class,
        DailyActivity::class,
        ExerciseCatalog::class,
        WorkoutSession::class,
        WorkoutSet::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(FitlogConverters::class)
abstract class AppDatabase : RoomDatabase() {

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}