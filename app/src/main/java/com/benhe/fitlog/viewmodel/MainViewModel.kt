package com.benhe.fitlog.viewmodel

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
import com.benhe.fitlog.model.FoodCategory // 确保引入
import com.benhe.fitlog.model.FoodItem     // 确保引入
import com.benhe.fitlog.data.FoodCatalog   // 确保引入
import com.benhe.fitlog.model.BodyStatRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.firstOrNull // 必须导入这个扩展函数
import com.benhe.fitlog.data.local.entiy.WorkoutSession

/**
 * 应用的核心 ViewModel。
 * 负责管理主界面的 UI 状态，处理用户交互，并作为 UI 层与数据层（数据库、SharedPreferences）之间的桥梁。
 * 包含饮食记录、训练记录同步、身体状态计算和身体指标管理等核心逻辑。
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // ==================== 数据源初始化 ====================
    private val db = AppDatabase.getDatabase(application)
    /** 饮食记录 DAO */
    private val dietDao = db.dietDao()
    /** 每日活动/睡眠/强度 DAO */
    private val dailyActivityDao = db.dailyActivityDao()
    /** 训练记录 DAO */
    private val workoutDao = db.workoutDao()
    /** 身体成分（体重/体脂） DAO */
    private val bodyStatDao = db.bodyStatDao()

    // (注意：以下两个变量与上面的 bodyStatDao 和 dailyActivityDao 指向相同实例，建议在后续重构中合并使用上面的变量)
    private val dao = AppDatabase.getDatabase(application).bodyStatDao()
    private val activityDao = AppDatabase.getDatabase(application).dailyActivityDao()

    private val workoutRepository = com.benhe.fitlog.data.repository.WorkoutRepository(workoutDao)

    // ==================== UI 状态流 (StateFlow) ====================

    /**
     * 1. 实时监听最新的身体指标数据。
     * 用于 UI 侧边栏显示当前的体重和体脂率。
     * 如果没有数据，初始值为 null。
     */
    val latestBodyStat: StateFlow<BodyStatRecord?> = dao.getLatestStat()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /**
     * 2. 实时监听身体指标历史列表。
     * 用于 UI 左侧的折线图展示历史趋势。
     * 如果没有数据，初始值为空列表。
     */
    val bodyStatHistory: StateFlow<List<BodyStatRecord>> = dao.getHistoryStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // ==================== 内部变量与常量 ====================


    private val gson = Gson()
    private val PREFS_NAME = "user_prefs" // 用于存储用户偏好和自定义食物的 SharedPreferences 名称
    private val KEY_CUSTOM_FOODS = "custom_foods_list" // 自定义食物列表在 SharedPreferences 中的 Key

    /** 内部持有的自定义食物列表状态流 */
    private val _customFoodItems = MutableStateFlow<List<FoodItem>>(emptyList())

    // ==================== 自定义食物逻辑 ====================

    /**
     * 3. 【核心 UI 数据源】对外暴露的完整食物分类列表流。
     *
     * 它的作用是将硬编码的默认食物库 (`FoodCatalog.categories`) 与用户添加的自定义食物 (`_customFoodItems`) 进行合并。
     * 合并后的列表可以直接供 UI 的 ExpandableListView 使用。
     */
    val allFoodCategories: StateFlow<List<FoodCategory>> = _customFoodItems.map { customItems ->
        val defaultList = FoodCatalog.categories.toMutableList() // 获取默认列表的拷贝

        if (customItems.isNotEmpty()) {
            // 创建一个专门存放自定义食物的新分类
            val customCategory = FoodCategory(
                id = "custom_user",
                name = "我的常吃/自定义",
                items = customItems.reversed() // 将最新添加的食物排在前面
            )
            // 将自定义分类插到整个列表的最前面，方便用户查找
            defaultList.add(0, customCategory)
        }
        defaultList
    }.stateIn(viewModelScope, SharingStarted.Lazily, FoodCatalog.categories)

    /**
     * ViewModel 初始化块。
     * 在 ViewModel 创建时立即启动加载本地自定义食物数据的任务。
     */
    init {
        loadCustomFoods()
    }

    /**
     * 从 SharedPreferences 中异步加载用户保存的自定义食物列表。
     * 加载成功后更新 `_customFoodItems` 状态流。
     */
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

    /**
     * 添加一个新的自定义食物项。
     *
     * 操作步骤：
     * 1. 更新内存中的状态流 `_customFoodItems`，以便 UI 立即刷新。
     * 2. 将更新后的列表序列化为 JSON 字符串。
     * 3. 异步持久化保存到 SharedPreferences 中。
     *
     * @param item 新增的食物对象
     */
    fun addCustomFood(item: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. 更新内存
            val currentList = _customFoodItems.value.toMutableList()
            currentList.add(item)
            _customFoodItems.value = currentList

            // 2. 保存到本地 (SharedPreferences)
            val sharedPref = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val jsonString = gson.toJson(currentList)
            sharedPref.edit().putString(KEY_CUSTOM_FOODS, jsonString).apply()
        }
    }


    // ==================== 训练记录同步核心逻辑 ====================

    /**
     * 【核心修复后的方法】同步保存某一天的所有部位训练摘要记录。
     *
     * 采用策略：**“覆盖更新”** (Replace by Day)。
     * 1. 确保目标日期存在一个父级 `WorkoutSession`（如果没有则创建）。
     * 2. **删除**该日期下所有的旧 `WorkoutSet` 记录。
     * 3. 根据传入的 `drafts` 数据，批量插入新的 `WorkoutSet` 记录，并关联到步骤1中的 Session ID。
     *
     * 这种策略避免了复杂的更新逻辑，确保数据库状态与 UI提交的状态完全一致。
     *
     * @param dateString 目标日期字符串 (yyyy-MM-dd)
     * @param drafts 包含部位数据的 Map，格式为：Map<部位枚举, Pair<RPE评级(Int), 备注文本(String)>>
     */
    fun syncWorkoutSets(dateString: String, drafts: Map<BodyRegion, Pair<Int, String>>) {
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
            // 使用 firstOrNull() 从 Flow 中获取最新的 Session 数据（挂起函数）
            var session = workoutDao.getSessionByDate(targetDate).firstOrNull()

            val finalSessionId: Long = if (session != null) {
                // 场景 A: 数据库里已经有了那一天的 Session，直接获取它的 ID
                session.sessionId
            } else {
                // 场景 B: 还没有那一天的 Session，创建一个新的
                val newSession = WorkoutSession(
                    date = targetDate,
                    startTime = startOfDay // 使用当天的起始时间戳作为 Session 的时间
                )
                // 插入数据库，并【务必】获取返回的新生成的自增 ID
                workoutDao.insertSession(newSession)
            }

            // 3. 删除旧数据 (实现覆盖策略)
            // 执行按天删除操作，清空当天所有旧的子记录
            workoutDao.deleteSetsByDay(startOfDay, endOfDay)

            // 4. 构建并插入新的子记录列表
            drafts.forEach { (region, data) ->
                // 只处理有有效数据（评级大于0 或 备注不为空）的记录，避免存入垃圾数据
                if (data.first > 0 || data.second.isNotBlank()) {
                    val newSet = WorkoutSet(
                        sessionId = finalSessionId, // 【关键】：填入步骤2获取的真实父 ID，解决外键约束问题
                        region = region,
                        rpe = data.first, // 这里存的是 1-5 星评级
                        note = data.second, // 这里存的是手动输入的备注
                        timestamp = timestampForSet,
                        // 以下为兼容字段，当前汇总模式给予默认值 0
                        weight = 0f,
                        reps = 0,
                        exerciseId = "manual"
                    )
                    // 执行插入操作
                    workoutDao.insertSet(newSet)
                }
            }
        }
    }


    // ==================== 身体负荷与恢复状态计算 ====================

    // 获取用于计算的基础数据流（默认使用今天日期）
    private val selectedDateString = LocalDate.now().toString()

    /**
     * 【核心功能】身体各部位恢复状态流。
     *
     * 这是一个组合流 (Combined Flow)，它实时监听以下三个数据源的变化：
     * 1. 最近的训练记录 (`workoutRepository.getRecentSetsFlow()`)
     * 2. 当天的睡眠和强度设置 (`dailyActivityDao`)
     * 3. 当天的蛋白质摄入总量 (`dietDao`)
     *
     * 每当这三者任意一个发生变化时，它会调用 `LoadCalculator.calculateRegionStatus` 重新计算所有部位当前的恢复系数 (0.0 - 1.0)。
     * 初始值为所有部位满状态 (1.0f)。
     */
    val bodyStatus: StateFlow<Map<BodyRegion, Float>> = combine(
        workoutRepository.getRecentSetsFlow(),
        dailyActivityDao.getActivityByDate(selectedDateString),
        dietDao.getTotalProteinForDate(selectedDateString)
    ) { sets, activity, protein ->
        LoadCalculator.calculateRegionStatus(
            sets = sets,
            userProfile = getUserProfile(), // 实时获取用户基础信息
            sleepHours = activity?.sleepHours ?: 8f, // 默认睡8小时
            intensity = activity?.intensity ?: LifeIntensity.NORMAL, // 默认中等生活强度
            isAfterburnActive = activity?.isAfterburnEnabled ?: false, // 是否处于后燃期
            proteinGrams = (protein ?: 0.0).toFloat(),
            targetTimestamp = System.currentTimeMillis() // ✅ 确保使用当前系统时间进行实时计算
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BodyRegion.entries.associateWith { 1.0f })

    /**
     * 自动化后燃状态流。
     *
     * 基于 `bodyStatus` 的计算结果衍生而来。如果当前任意一个身体部位的恢复系数低于 0.5（例如深度疲劳），
     * 则自动判定为处于“后燃效应”活跃状态 (true)，否则为 false。
     * 这解决了手动设置后燃开关容易遗忘的问题。
     */
    val isAfterburnAutoActive: StateFlow<Boolean> = bodyStatus
        .map { statusMap -> statusMap.values.any { it < 0.5f } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    // ==================== 用户资料与热量消耗计算 ====================

    /**
     * 从 SharedPreferences 中读取用户的基础资料（体重、身高、年龄、性别）。
     * 包含对体重和身高单位的自动纠错逻辑（防止用户错误输入克或毫米）。
     * @return UserProfile 对象
     */
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

    /**
     * 计算今日的总热量消耗 (TDEE)。
     *
     * 基于用户的基础代谢率 (BMR)，结合当天的生活强度系数和后燃状态进行计算。
     *
     * @param activity 当天的活动记录对象。如果为空，则使用默认强度，并使用自动计算的后燃状态 (`isAfterburnAutoActive`)。
     * @return 估算的每日总消耗热量 (Kcal)
     */
    fun getTodayExpenditure(activity: DailyActivity?): Int {
        val profile = getUserProfile()

        val bmr = HealthCalculator.calcBMR(profile)

        // 使用自动检测的后燃状态。如果 activity 为空，默认参考当前 bodyStatus 算出的自动化值
        val afterburn = activity?.isAfterburnEnabled ?: isAfterburnAutoActive.value

        return HealthCalculator.calculateDailyExpenditure(
            bmr = bmr,
            intensity = activity?.intensity ?: LifeIntensity.NORMAL,
            isAfterburnEnabled = afterburn
        )
    }

    /**
     * 当用户在 UI 上确认修改了睡眠和强度时调用此方法。
     * 它会使用当前自动计算的后燃状态 (`isAfterburnAutoActive`) 来更新数据库。
     */
    fun onActivityConfirm(date: String, sleep: Float, intensity: LifeIntensity) {
        // 直接调用更新逻辑，去掉了手动 afterburn 参数，改用自动值
        updateActivityForDate(date, sleep, intensity)
    }

    /**
     * 内部方法：更新指定日期的每日状态记录。
     * 使用自动计算的后燃状态值。
     */
    private fun updateActivityForDate(date: String, sleep: Float, intensity: LifeIntensity) {
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



    /**
     * 获取指定日期的每日活动状态流。
     */
    fun getActivityForDate(date: String): Flow<DailyActivity?> =
        dailyActivityDao.getActivityByDate(date)


    // ==================== 饮食记录相关逻辑 ====================

    /**
     * 保存一条新的饮食记录到数据库。
     * @param foodName 食物名称
     * @param category 分类ID
     * @param quantity 摄入量描述（如 "150g"）
     * @param calories 热量 (kcal)
     * @param protein 蛋白质 (g)
     * @param carbs 碳水化合物 (g)
     * @param date 日期字符串
     */
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

    /** 获取指定日期的总摄入热量流 */
    fun getTotalCaloriesForDate(date: String): Flow<Double> =
        dietDao.getTotalCaloriesByDate(date).map { it ?: 0.0 }

    /** 获取指定日期的总摄入蛋白质流 */
    fun getTotalProteinForDate(date: String): Flow<Double> =
        dietDao.getTotalProteinForDate(date).map { it ?: 0.0 }

    /** 获取指定日期的总摄入碳水流 */
    fun getTotalCarbsForDate(date: String): Flow<Double> =
        dietDao.getTotalCarbsByDate(date).map { it ?: 0.0 }

    /** 获取指定日期的所有饮食记录列表流 */
    fun getDietRecordsForDate(date: String): Flow<List<DietRecord>> =
        dietDao.getRecordsByDate(date)

    /** 删除一条指定的饮食记录 */
    fun deleteDietRecord(record: DietRecord) {
        viewModelScope.launch(Dispatchers.IO) { dietDao.deleteRecord(record) }
    }


    // ==================== 训练记录读取与旧版保存逻辑 ====================

    /**
     * 获取指定日期范围内的所有训练子记录 (WorkoutSet)。
     * 通过计算该日期的起始和结束时间戳来进行范围查询。
     */
    fun getSetsByDate(date: String): Flow<List<WorkoutSet>> {
        val localDate = java.time.LocalDate.parse(date)
        // 计算当天的起始时间戳 (00:00:00)
        val start = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        // 计算当天的结束时间戳 (23:59:59.999)
        val end = localDate.plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
        // 调用 Dao 里的范围查询
        return workoutDao.getSetsByTimeRange(start, end)
    }


    // ==================== 身体指标保存逻辑 ====================

    /**
     * 保存当前的体重和体脂率数据。
     *
     * 策略：为了数据整洁和图表展示方便，会将时间戳对齐到当前的**整点**（例如 14:35 保存，实际记录时间为 14:00:00）。
     *
     * @param weight 体重 (kg)
     * @param bodyFat 体脂率 (%)
     */
    fun saveBodyStat(weight: Float, bodyFat: Float) {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            // 设为整点：分钟0，秒0，纳秒0
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
}