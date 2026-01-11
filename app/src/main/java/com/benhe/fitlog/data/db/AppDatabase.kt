package com.benhe.fitlog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DietRecord::class],
    version = 2, // ✅ 确保版本号比之前大（如果之前是1，现在改2）
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dietDao(): DietDao

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
                    .fallbackToDestructiveMigration() // ✅ 关键！如果结构变了，自动清空旧数据重建，防止闪退
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}