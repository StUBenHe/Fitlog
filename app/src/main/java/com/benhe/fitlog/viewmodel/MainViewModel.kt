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
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.ZoneId


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


    private val currentSleepHours = 8f
    private val currentProtein = 100f




// 1. 获取基础数据流
    private val selectedDate = LocalDate.now().toString() // 或你当前选中的日期
    val dailyActivityFlow = dailyActivityDao.getActivityByDate(selectedDate)
    val totalProteinFlow = dietDao.getTotalProteinForDate(selectedDate)
    // 这样 UI 就能通过 viewModel.bodyStatus 监听到最新的恢复状态

    private val selectedDateString = LocalDate.now().toString()
    val bodyStatus: StateFlow<Map<BodyRegion, Float>> = combine(
        workoutRepository.getRecentSetsFlow(),
        dailyActivityDao.getActivityByDate(selectedDateString),
        dietDao.getTotalProteinForDate(selectedDateString)
    ) { sets, activity, protein ->
        LoadCalculator.calculateRegionStatus(
            sets = sets,
            userProfile = getUserProfile(),
            sleepHours = activity?.sleepHours ?: 8f,
            intensity = activity?.intensity ?: LifeIntensity.NORMAL,
            isAfterburnActive = activity?.isAfterburnEnabled ?: false,
            proteinGrams = (protein ?: 0.0).toFloat(),
            targetTimestamp = System.currentTimeMillis() // ✅ 确保使用当前时间进行计算
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BodyRegion.entries.associateWith { 1.0f })

    // B. 自动化后燃状态流：基于 bodyStatus 计算 (解决了 Variable must be initialized 错误)
    val isAfterburnAutoActive: StateFlow<Boolean> = bodyStatus
        .map { statusMap -> statusMap.values.any { it < 0.5f } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    /**
     * 获取用户资料 (修正后的私有方法)
     */


    /**
     * 计算今日消耗 (修正参数报错)
     */
    fun getTodayExpenditure(activity: DailyActivity?): Int {
        val profile = getUserProfile()

        val bmr = HealthCalculator.calcBMR(profile)

        // 使用自动检测的后燃状态，如果没有 activity 数据，默认参考当前 bodyStatus 算出的自动化值
        val afterburn = activity?.isAfterburnEnabled ?: isAfterburnAutoActive.value

        return HealthCalculator.calculateDailyExpenditure(
            bmr = bmr,
            intensity = activity?.intensity ?: LifeIntensity.NORMAL,
            isAfterburnEnabled = afterburn
        )
    }
    /**
     * 更新每日状态 (确认时调用)
     */
    fun updateActivityForDate(date: String, sleep: Float, intensity: LifeIntensity) {
        viewModelScope.launch(Dispatchers.IO) {
            // ✅ 后燃开关改为自动判断逻辑：如果当前 bodyStatus 有部位 < 0.5，存入数据库时设为 true
            val autoAfterburn = isAfterburnAutoActive.value

            val record = DailyActivity(
                date = date,
                sleepHours = sleep,
                intensity = intensity,
                isAfterburnEnabled = autoAfterburn
            )
            dailyActivityDao.insertOrUpdateActivity(record)
        }
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
            val selectedLocalDate = LocalDate.parse(date)
            val now = java.time.LocalDateTime.now()

            // ✅ 修正：对齐整点时间戳
            val timestamp = if (selectedLocalDate.isEqual(now.toLocalDate())) {
                // 场景：记录今天 -> 取当前小时的整点（例如 22:35 -> 22:00）
                now.withMinute(0).withSecond(0).withNano(0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            } else {
                // 场景：补录过去 -> 默认取那天晚上的 20:00
                selectedLocalDate.atTime(20, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }

            // 删除旧记录逻辑 (按天范围删除)
            val startOfDay = selectedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDay = startOfDay + 86400000L - 1
            workoutDao.deleteSetsByDay(startOfDay, endOfDay)

            // 批量插入
            records.forEach { (region, data) ->
                if (data.first > 0 || data.second.isNotBlank()) {
                    workoutDao.insertSet(WorkoutSet(
                        region = region,
                        rpe = data.first,
                        note = data.second,
                        timestamp = timestamp, // ✅ 存入整点时间戳
                        weight = 0f, reps = 0, sessionId = 0, exerciseId = "manual"
                    ))
                }
            }
        }
    }

    // 修正获取 Profile 逻辑，防止单位错误
    private fun getUserProfile(): UserProfile {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        var weight = sharedPref.getString("weight", "70.0")?.toDoubleOrNull() ?: 70.0
        var height = sharedPref.getString("height", "180.0")?.toDoubleOrNull() ?: 180.0

        // 自动纠正单位错误（如果是克或毫米）
        if (weight > 500) weight /= 1000
        if (height > 1000) height /= 10

        return UserProfile(weight, height,
            sharedPref.getString("age", "27")?.toIntOrNull() ?: 27,
            sharedPref.getString("gender", "男") ?: "男"
        )
    }
    fun onActivityConfirm(date: String, sleep: Float, intensity: LifeIntensity) {
        // 直接调用更新逻辑，去掉了手动 afterburn 参数
        updateActivityForDate(date, sleep, intensity)
    }
}
