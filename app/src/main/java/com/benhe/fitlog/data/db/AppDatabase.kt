package com.benhe.fitlog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DietRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dietDao(): DietDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 在 AppDatabase.kt 中
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitlog_database"
                )
                    .fallbackToDestructiveMigration() // ✅ 添加这一行，防止表结构修改后崩溃
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}