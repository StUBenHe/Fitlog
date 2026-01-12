package com.benhe.fitlog.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.benhe.fitlog.data.db.AppDatabase
import com.benhe.fitlog.data.db.DietRecord
import com.benhe.fitlog.logic.HealthCalculator
import com.benhe.fitlog.model.BodyRegion
import com.benhe.fitlog.model.DailyActivity
import com.benhe.fitlog.model.LifeIntensity
import com.benhe.fitlog.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.benhe.fitlog.data.entity.WorkoutSet
import com.benhe.fitlog.logic.LoadCalculator

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dietDao = db.dietDao()
    private val dailyActivityDao = db.dailyActivityDao()
    private val workoutDao = db.workoutDao()

    // 1. 初始化 Repository
    private val workoutRepository = com.benhe.fitlog.data.repository.WorkoutRepository(workoutDao)

    /**
     * 2. 获取 8 大区块负荷状态
     * 解决 Cannot infer type 的关键：显式指定 emptyMap 的类型
     */
// MainViewModel.kt

    private val currentSleepHours = 8f
    private val currentProtein = 100f

    // MainViewModel.kt

    // 这样 UI 就能通过 viewModel.bodyStatus 监听到最新的恢复状态
    val bodyStatus: StateFlow<Map<BodyRegion, Float>> = workoutRepository.getBodyStatusFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BodyRegion.entries.associateWith { 1.0f } // 初始满血
        )

    /**
     * 3. 提取用户资料 (用于计算)
     */
    private fun getUserProfile(): UserProfile {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return UserProfile(
            weight = sharedPref.getString("weight", "70.0")?.toDoubleOrNull() ?: 70.0,
            height = sharedPref.getString("height", "180.0")?.toDoubleOrNull() ?: 180.0,
            age = sharedPref.getString("age", "27")?.toIntOrNull() ?: 27,
            gender = sharedPref.getString("gender", "男") ?: "男"
        )
    }

    /**
     * 4. 计算今日消耗 (由 UI 调用)
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
            isAfterburnEnabled = activity?.isAfterburnEnabled ?: false
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

    fun updateActivityForDate(date: String, sleep: Float, intensity: LifeIntensity, afterburn: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val record = DailyActivity(
                date = date,
                sleepHours = sleep,
                intensity = intensity,
                isAfterburnEnabled = afterburn
            )
            dailyActivityDao.insertOrUpdateActivity(record)
        }
    }

    // 获取指定日期的所有训练记录
    fun getSetsByDate(date: String): Flow<List<WorkoutSet>> {
        val localDate = java.time.LocalDate.parse(date)
        val start = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = localDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
        // 调用 Dao 里的范围查询
        return workoutDao.getSetsByTimeRange(start, end)
    }
    // 在 MainViewModel.kt 中更新
    fun saveWorkoutSet(date: String, region: BodyRegion, weight: Float, reps: Int, rpe: Int, note: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val timestamp = java.time.LocalDate.parse(date)
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            // 逻辑：如果该部位今天已经有记录了，我们覆盖它（因为现在是“汇总模式”）
            // 也可以简单地插入新的一条，反正 UI 只取最后一条。
            val set = WorkoutSet(
                sessionId = 0,
                region = region,
                exerciseId = note, // 我们直接用 exerciseId 这个 String 存你的手动备注
                weight = weight,
                reps = reps,
                rpe = rpe, // 这里的 rpe 对应 1-5 星
                timestamp = timestamp
            )
            workoutDao.insertSet(set)
        }
    }
    fun syncWorkoutSets(date: String, records: Map<BodyRegion, Pair<Int, String>>) {
        viewModelScope.launch(Dispatchers.IO) {
            val localDate = java.time.LocalDate.parse(date)
            val startOfDay = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDay = localDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

            // 1. 先清理该日期所有旧记录
            workoutDao.deleteSetsByDay(startOfDay, endOfDay)

            // 2. 批量插入有内容的记录
            records.forEach { (region, data) ->
                if (data.first > 0 || data.second.isNotBlank()) {
                    val set = WorkoutSet(
                        sessionId = 0,             // 补全参数
                        region = region,
                        exerciseId = "manual_entry",// 补全参数
                        weight = 0f,               // 补全参数
                        reps = 0,                  // 补全参数
                        rpe = data.first,
                        note = data.second,
                        timestamp = startOfDay
                    )
                    workoutDao.insertSet(set)
                }
            }
        }
    }

}