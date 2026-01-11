package com.benhe.fitlog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.benhe.fitlog.data.DailyActivityDao
import com.benhe.fitlog.model.DailyActivity
import com.benhe.fitlog.data.db.DietRecord

@Database(
    entities = [DietRecord::class, DailyActivity::class], // ✅ 这里添加了 DailyActivity
    version = 3, // ✅ 版本号改为 3（必须比之前大）
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dietDao(): DietDao
    abstract fun dailyActivityDao(): DailyActivityDao // ✅ 这里添加了 Dao 接口

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
                    .fallbackToDestructiveMigration() // ✅ 结构改变时自动重建数据库
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}