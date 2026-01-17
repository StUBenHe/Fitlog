package com.benhe.fitlog.viewmodel


import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.benhe.fitlog.data.local.AppDatabase
import com.benhe.fitlog.data.local.entiy.DietRecord
import com.benhe.fitlog.util.HealthCalculator
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
import com.benhe.fitlog.data.local.entiy.WorkoutSet
import com.benhe.fitlog.util.LoadCalculator
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.ZoneId
import com.benhe.fitlog.model.FoodCategory
import com.benhe.fitlog.model.FoodItem
import com.benhe.fitlog.data.FoodCatalog
import com.benhe.fitlog.model.BodyStatRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.firstOrNull
import com.benhe.fitlog.data.local.entiy.WorkoutSession
import com.benhe.fitlog.model.PeriodDay
import com.benhe.fitlog.data.local.dao.PeriodDao
// 删除：不再需要引入 WorkoutItemDraft
// import com.benhe.fitlog.ui.WorkoutItemDraft
import com.benhe.fitlog.ui.theme.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf


/**
 * 应用的核心 ViewModel。
 * 负责管理主界面的 UI 状态，处理用户交互，并作为 UI 层与数据层之间的桥梁。
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // ==================== 数据源初始化 ====================
    private val db = AppDatabase.getDatabase(application)
    private val dietDao = db.dietDao()
    private val dailyActivityDao = db.dailyActivityDao()
    private val workoutDao = db.workoutDao()
    private val bodyStatDao = db.bodyStatDao()
    private val periodDao: PeriodDao = db.periodDao()

    // (注意：以下两个变量与上面的 bodyStatDao 和 dailyActivityDao 指向相同实例，建议合并)
    private val dao = AppDatabase.getDatabase(application).bodyStatDao()
    private val activityDao = AppDatabase.getDatabase(application).dailyActivityDao()

    private val workoutRepository = com.benhe.fitlog.data.repository.WorkoutRepository(workoutDao)

    // ==================== UI 状态管理 (解决页面切换数据丢失问题) ====================

    /** 保存身体部位的编辑状态 (Key: 部位, Value: Pair<等级, 备注>) */
    val regionDraftState = mutableStateMapOf<BodyRegion, Pair<Int, String>>()

    // 删除：保存自定义项目的编辑列表状态
    // val customItemsDraftState = mutableStateListOf<WorkoutItemDraft>()

    /** 清空所有训练记录草稿 */
    fun clearWorkoutDrafts() {
        regionDraftState.clear()
        // 删除：清空自定义项目草稿
        // customItemsDraftState.clear()
    }


    // ==================== UI 状态流 (StateFlow) ====================

    /** 实时监听最新的身体指标数据 (侧边栏使用) */
    val latestBodyStat: StateFlow<BodyStatRecord?> = dao.getLatestStat()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** 实时监听身体指标历史列表 (左侧折线图使用) */
    val bodyStatHistory: StateFlow<List<BodyStatRecord>> = dao.getHistoryStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ==================== 经期追踪逻辑 ====================

    /** 获取所有历史经期日期集合 (深色标记) */
    val pastPeriodDates: StateFlow<Set<LocalDate>> = periodDao.getAllPeriodDays()
        .map { list -> list.map { it.date }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    /** 获取预测的未来经期日期集合 (浅色标记) */
    val predictedPeriodDates: StateFlow<Set<LocalDate>> = periodDao.getAllPeriodDays()
        .map { historyList -> calculatePrediction(historyList) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    /** 经期预测算法 (基础版: 周期28天, 持续5天) */
    private fun calculatePrediction(history: List<PeriodDay>): Set<LocalDate> {
        if (history.isEmpty()) return emptySet()
        val lastCycleStartDate = history.first().date
        val defaultCycleLength = 28L
        val defaultDuration = 5L
        val nextCycleStart = lastCycleStartDate.plusDays(defaultCycleLength)
        val predictedSet = mutableSetOf<LocalDate>()
        for (i in 0 until defaultDuration) {
            predictedSet.add(nextCycleStart.plusDays(i))
        }
        return predictedSet
    }

    /** 切换指定日期的经期状态 */
    fun togglePeriodStatus(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val isMarked = periodDao.isPeriodDay(date)
            if (isMarked) {
                periodDao.delete(PeriodDay(date))
            } else {
                periodDao.insert(PeriodDay(date))
            }
        }
    }

    // ==================== 内部变量与自定义食物逻辑 ====================

    private val gson = Gson()
    private val PREFS_NAME = "user_prefs"
    private val KEY_CUSTOM_FOODS = "custom_foods_list"
    private val _customFoodItems = MutableStateFlow<List<FoodItem>>(emptyList())

    /** 完整食物分类列表 (默认+自定义) */
    val allFoodCategories: StateFlow<List<FoodCategory>> = _customFoodItems.map { customItems ->
        val defaultList = FoodCatalog.categories.toMutableList()
        if (customItems.isNotEmpty()) {
            val customCategory = FoodCategory(
                id = "custom_user",
                name = "我的常吃/自定义",
                items = customItems.reversed()
            )
            defaultList.add(0, customCategory)
        }
        defaultList
    }.stateIn(viewModelScope, SharingStarted.Lazily, FoodCatalog.categories)

    init {
        loadCustomFoods()
    }

    private fun loadCustomFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            val sharedPref = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val json = sharedPref.getString(KEY_CUSTOM_FOODS, null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<FoodItem>>() {}.type
                    val list: List<FoodItem> = gson.fromJson(json, type)
                    _customFoodItems.value = list
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun addCustomFood(item: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentList = _customFoodItems.value.toMutableList()
            currentList.add(item)
            _customFoodItems.value = currentList
            val sharedPref = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val jsonString = gson.toJson(currentList)
            sharedPref.edit().putString(KEY_CUSTOM_FOODS, jsonString).apply()
        }
    }


    // ==================== 训练记录同步核心逻辑 ====================

    /**
     * 【核心修复后的方法】同步保存某一天的所有部位训练摘要记录。
     * 采用策略：**“覆盖更新”** (Replace by Day)。
     *
     * @param dateString 目标日期字符串 (yyyy-MM-dd)
     * @param drafts 包含部位数据的 Map (UI 状态 regionDraftState)
     * // 修改：移除了 customItems 参数
     */
    // 修改：方法签名移除了 customItems: List<WorkoutItemDraft>
    fun syncWorkoutSets(
        dateString: String,
        drafts: Map<BodyRegion, Pair<Int, String>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. 【准备工作：提前计算好所有需要的时间变量】
            val targetDate = LocalDate.parse(dateString)
            val zoneId = ZoneId.systemDefault()
            val now = LocalDateTime.now()

            // 计算那一天的开始和结束时间戳 (用于精准删除旧数据和创建新 Session 的 startTime)
            val startOfDay = targetDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
            val endOfDay = startOfDay + 86400000L - 1

            // 计算用于子表 WorkoutSet 的具体时间戳
            val timestampForSet = if (targetDate.isEqual(now.toLocalDate())) {
                // 如果是今天，用当前时间的整点（分钟和秒置零）
                now.withMinute(0).withSecond(0).withNano(0)
                    .atZone(zoneId).toInstant().toEpochMilli()
            } else {
                // 如果是补录过去，统一归档到那天晚上的 20:00:00
                targetDate.atTime(20, 0)
                    .atZone(zoneId).toInstant().toEpochMilli()
            }

            // 2. 获取或创建真实存在的父 Session ID
            var session = workoutDao.getSessionByDate(targetDate).firstOrNull()
            val finalSessionId: Long = if (session != null) {
                session.sessionId
            } else {
                val newSession = WorkoutSession(
                    date = targetDate,
                    startTime = startOfDay
                )
                workoutDao.insertSession(newSession)
            }

            // 3. 删除旧数据 (实现覆盖策略)
            workoutDao.deleteSetsByDay(startOfDay, endOfDay)

            // 4. 构建并插入新的子记录列表 (身体部位部分)
            drafts.forEach { (region, data) ->
                if (data.first > 0 || data.second.isNotBlank()) {
                    val newSet = WorkoutSet(
                        sessionId = finalSessionId,
                        region = region,
                        rpe = data.first,
                        note = data.second,
                        timestamp = timestampForSet,
                        weight = 0f,
                        reps = 0,
                        exerciseId = "manual_region" // ✅ 标识为身体部位记录
                    )
                    workoutDao.insertSet(newSet)
                }
            }

            // 删除：移除了原有的 "5. 构建并插入新的子记录列表 (自定义项目部分)" 整个代码块
        }
    }

    // ==================== 身体负荷与恢复状态计算 ====================

    private val selectedDateString = LocalDate.now().toString()

    /** 身体各部位恢复状态流 (0.0 - 1.0) */
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
            targetTimestamp = System.currentTimeMillis()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BodyRegion.entries.associateWith { 1.0f })

    /** 自动化后燃状态流 (基于 bodyStatus) */
    val isAfterburnAutoActive: StateFlow<Boolean> = bodyStatus
        .map { statusMap -> statusMap.values.any { it < 0.5f } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    // ==================== 用户资料与热量消耗计算 ====================

    private fun getUserProfile(): UserProfile {
        val sharedPref = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        var weight = sharedPref.getString("weight", "70.0")?.toDoubleOrNull() ?: 70.0
        var height = sharedPref.getString("height", "180.0")?.toDoubleOrNull() ?: 180.0
        if (weight > 500) weight /= 1000
        if (height > 1000) height /= 10
        return UserProfile(weight, height,
            sharedPref.getString("age", "27")?.toIntOrNull() ?: 27,
            sharedPref.getString("gender", "男") ?: "男"
        )
    }

    /** 计算今日的总热量消耗 (TDEE) */
    fun getTodayExpenditure(activity: DailyActivity?): Int {
        val profile = getUserProfile()
        val bmr = HealthCalculator.calcBMR(profile)
        val afterburn = activity?.isAfterburnEnabled ?: isAfterburnAutoActive.value
        return HealthCalculator.calculateDailyExpenditure(
            bmr = bmr,
            intensity = activity?.intensity ?: LifeIntensity.NORMAL,
            isAfterburnEnabled = afterburn
        )
    }

    /** 确认修改每日活动状态 */
    fun onActivityConfirm(date: String, sleep: Float, intensity: LifeIntensity) {
        updateActivityForDate(date, sleep, intensity)
    }

    private fun updateActivityForDate(date: String, sleep: Float, intensity: LifeIntensity) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun getActivityForDate(date: String): Flow<DailyActivity?> =
        dailyActivityDao.getActivityByDate(date)


    // ==================== 饮食记录相关逻辑 ====================

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


    // ==================== 训练记录读取 ====================

    /** 获取指定日期范围内的所有训练子记录 */
    fun getSetsByDate(date: String): Flow<List<WorkoutSet>> {
        val localDate = java.time.LocalDate.parse(date)
        val start = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = localDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
        return workoutDao.getSetsByTimeRange(start, end)
    }


    // ==================== 身体指标保存逻辑 ====================

    fun saveBodyStat(weight: Float, bodyFat: Float) {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            val onTheHour = now.withMinute(0).withSecond(0).withNano(0)
            val timestamp = onTheHour.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val dateStr = onTheHour.format(DateTimeFormatter.ofPattern("MM-dd"))
            val record = BodyStatRecord(
                timestamp = timestamp,
                weight = weight,
                bodyFatRate = bodyFat,
                dateString = dateStr
            )
            dao.insertStat(record)
        }
    }

    fun loadWorkoutDraftsForDate(dateString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. 复用已有的方法获取当天的数据流，并取第一个值（相当于一次性查询）
            // 使用 first() 会挂起直到获取到最新的一份数据列表
            val sets = getSetsByDate(dateString).first()

            // 2. 切换到主线程更新 UI 状态集合 (Compose 的状态必须在主线程更新)
            withContext(Dispatchers.Main) {
                // 先清空当前状态，防止残留
                regionDraftState.clear()
                // 遍历查询到的记录並填充状态
                sets.forEach { set ->
                    // 只处理部位记录 (在保存时我们将 exerciseId 设为了 "manual_region")
                    if (set.exerciseId == "manual_region") {
                        // 将数据库存储的 RPE 和 Note 重新放入 UI 状态 Map 中
                        // 这样 UI 就能显示出之前保存的数据了
                        // 使用 ?: 操作符处理空值。如果数据库里是 null，就用 0 和 "" 代替
                        regionDraftState[set.region] = Pair(set.rpe ?: 0, set.note ?: "")
                    }
                }
            }
        }
    }

}