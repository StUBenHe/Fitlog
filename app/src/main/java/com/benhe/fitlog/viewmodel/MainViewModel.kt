package com.benhe.fitlog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.benhe.fitlog.data.db.AppDatabase
import com.benhe.fitlog.data.db.DietRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dietDao = AppDatabase.getDatabase(application).dietDao()

    // ✅ 修改后的保存方法，包含所有必要字段
    fun saveDietRecord(
        foodName: String,
        category: String,
        quantity: String,
        calories: Double,
        protein: Double,
        date: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val record = DietRecord(
                foodName = foodName,
                category = category,
                quantity = quantity,
                calories = calories,
                protein = protein,
                date = date,
                timestamp = System.currentTimeMillis() // 自动生成时间戳
            )
            dietDao.insertRecord(record)
        }
    }

    fun getTotalCaloriesForDate(date: String): Flow<Double> {
        return dietDao.getTotalCaloriesByDate(date).map { it ?: 0.0 }
    }

    fun getTotalProteinForDate(date: String): Flow<Double> {
        return dietDao.getTotalProteinForDate(date).map { it ?: 0.0 }
    }

    fun getDietRecordsForDate(date: String): Flow<List<DietRecord>> {
        return dietDao.getRecordsByDate(date)
    }
}