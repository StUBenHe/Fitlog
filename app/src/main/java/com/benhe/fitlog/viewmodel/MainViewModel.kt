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
        carbs: Double, // ✅ 增加参数
        date: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val record = DietRecord(
                foodName = foodName,
                category = category,
                quantity = quantity,
                calories = calories,
                protein = protein,
                carbs = carbs, // ✅ 赋值
                date = date,
                timestamp = System.currentTimeMillis()
            )
            dietDao.insertRecord(record)
        }
    }

    // 增加碳水查询
    fun getTotalCaloriesForDate(date: String): Flow<Double> {
        return dietDao.getTotalCaloriesByDate(date).map { it ?: 0.0 }
    }

    fun getTotalProteinForDate(date: String): Flow<Double> {
        return dietDao.getTotalProteinForDate(date).map { it ?: 0.0 }
    }

    fun getTotalCarbsForDate(date: String): Flow<Double> {
        return dietDao.getTotalCarbsByDate(date).map { it ?: 0.0 }
    }

    // 在 MainViewModel 类中确保有这个方法
    fun getDietRecordsForDate(date: String): Flow<List<DietRecord>> {
        return dietDao.getRecordsByDate(date)
    }

    // 在 MainViewModel 类中添加
    fun deleteDietRecord(record: DietRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            dietDao.deleteRecord(record)
        }
    }

}