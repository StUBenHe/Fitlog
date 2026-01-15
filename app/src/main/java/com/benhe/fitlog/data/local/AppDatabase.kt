package com.benhe.fitlog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.benhe.fitlog.data.local.dao.DailyActivityDao
import com.benhe.fitlog.data.local.FitlogConverters
import com.benhe.fitlog.data.local.dao.DietDao
import com.benhe.fitlog.data.local.entiy.DietRecord
import com.benhe.fitlog.data.local.dao.BodyStatDao
import com.benhe.fitlog.data.local.dao.WorkoutDao
import com.benhe.fitlog.data.local.entiy.ExerciseCatalog
import com.benhe.fitlog.data.local.entiy.WorkoutSession
import com.benhe.fitlog.data.local.entiy.WorkoutSet
import com.benhe.fitlog.model.BodyStatRecord
import com.benhe.fitlog.model.DailyActivity

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