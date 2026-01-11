package com.benhe.fitlog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.benhe.fitlog.data.db.AppDatabase
import com.benhe.fitlog.data.db.DietRecord
import com.benhe.fitlog.logic.HealthCalculator
import com.benhe.fitlog.model.DailyActivity
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dietDao = db.dietDao()
    private val dailyActivityDao = db.dailyActivityDao()

    /**
     * 1. 提取用户资料 (用于计算)
     */
    private fun getUserProfile(): UserProfile {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        return UserProfile(
            weight = sharedPref.getString("weight", "70.0")?.toDoubleOrNull() ?: 70.0,
            height = sharedPref.getString("height", "180.0")?.toDoubleOrNull() ?: 180.0,
            age = sharedPref.getString("age", "27")?.toIntOrNull() ?: 27,
            gender = sharedPref.getString("gender", "男") ?: "男"
        )
    }

    /**
     * 2. 计算今日消耗 (由 UI 调用)
     */
    fun getTodayExpenditure(activity: DailyActivity?): Int {
        val profile = getUserProfile()
        val bmr = HealthCalculator.calcBMR(
            weight = profile.weight,
            height = profile.height,
            age = profile.age,
            gender = profile.gender
        )
        return HealthCalculator.calculateDailyExpenditure(
            bmr = bmr,
            intensity = activity?.intensity ?: LifeIntensity.NORMAL,
            isAfterburnEnabled = activity?.isAfterburnEnabled ?: false // ✅ 这里的报错会因为修改了 Model 而消失
        )
    }

    // --- 饮食记录逻辑 ---

    fun saveDietRecord(
        foodName: String, category: String, quantity: String,
        calories: Double, protein: Double, carbs: Double, date: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val record = DietRecord(
                foodName = foodName, category = category, quantity = quantity,
                calories = calories, protein = protein, carbs = carbs,
                date = date, timestamp = System.currentTimeMillis()
            )
            dietDao.insertRecord(record)
        }
    }

    fun getTotalCaloriesForDate(date: String): Flow<Double> =
        dietDao.getTotalCaloriesByDate(date).map { it ?: 0.0 }

    fun getTotalProteinForDate(date: String): Flow<Double> =
        dietDao.getTotalProteinForDate(date).map { it ?: 0.0 }

    fun getTotalCarbsForDate(date: String): Flow<Double> =
        dietDao.getTotalCarbsByDate(date).map { it ?: 0.0 }

    fun getDietRecordsForDate(date: String): Flow<List<DietRecord>> =
        dietDao.getRecordsByDate(date)

    fun deleteDietRecord(record: DietRecord) {
        viewModelScope.launch(Dispatchers.IO) { dietDao.deleteRecord(record) }
    }

    // --- 每日状态逻辑 ---

    fun getActivityForDate(date: String): Flow<DailyActivity?> =
        dailyActivityDao.getActivityByDate(date)

    // ✅ 修改更新方法：增加 afterburn 参数，保证数据完整写入数据库
    fun updateActivityForDate(date: String, sleep: Float, intensity: LifeIntensity, afterburn: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val record = DailyActivity(
                date = date,
                sleepHours = sleep,
                intensity = intensity,
                isAfterburnEnabled = afterburn // ✅ 写入后燃开关状态
            )
            dailyActivityDao.insertOrUpdateActivity(record)
        }
    }
}