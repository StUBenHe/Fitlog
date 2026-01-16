package com.benhe.fitlog.viewmodel;

/**
 * 应用的核心 ViewModel。
 * 负责管理主界面的 UI 状态，处理用户交互，并作为 UI 层与数据层（数据库、SharedPreferences）之间的桥梁。
 * 包含饮食记录、训练记录同步、身体状态计算和身体指标管理等核心逻辑。
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00c6\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u000bJ\u000e\u00101\u001a\u00020/2\u0006\u00102\u001a\u000203J\u0016\u00104\u001a\n\u0012\u0006\u0012\u0004\u0018\u000106052\u0006\u00107\u001a\u00020\u0006J\u001a\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030\n052\u0006\u00107\u001a\u00020\u0006J\u001a\u00109\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0\n052\u0006\u00107\u001a\u00020\u0006J\u0010\u0010;\u001a\u00020<2\b\u0010=\u001a\u0004\u0018\u000106J\u0014\u0010>\u001a\b\u0012\u0004\u0012\u00020?052\u0006\u00107\u001a\u00020\u0006J\u0014\u0010@\u001a\b\u0012\u0004\u0012\u00020?052\u0006\u00107\u001a\u00020\u0006J\u0014\u0010A\u001a\b\u0012\u0004\u0012\u00020?052\u0006\u00107\u001a\u00020\u0006J\b\u0010B\u001a\u00020CH\u0002J\b\u0010D\u001a\u00020/H\u0002J\u001e\u0010E\u001a\u00020/2\u0006\u00107\u001a\u00020\u00062\u0006\u0010F\u001a\u00020\u001b2\u0006\u0010G\u001a\u00020HJ\u0016\u0010I\u001a\u00020/2\u0006\u0010J\u001a\u00020\u001b2\u0006\u0010K\u001a\u00020\u001bJ>\u0010L\u001a\u00020/2\u0006\u0010M\u001a\u00020\u00062\u0006\u0010N\u001a\u00020\u00062\u0006\u0010O\u001a\u00020\u00062\u0006\u0010P\u001a\u00020?2\u0006\u0010Q\u001a\u00020?2\u0006\u0010R\u001a\u00020?2\u0006\u00107\u001a\u00020\u0006J.\u0010S\u001a\u00020/2\u0006\u0010T\u001a\u00020\u00062\u001e\u0010U\u001a\u001a\u0012\u0004\u0012\u00020\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020<\u0012\u0004\u0012\u00020\u00060V0\u0019J \u0010W\u001a\u00020/2\u0006\u00107\u001a\u00020\u00062\u0006\u0010F\u001a\u00020\u001b2\u0006\u0010G\u001a\u00020HH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\n0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\n0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R#\u0010\u0018\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001b0\u00190\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012R\u000e\u0010\u001d\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020&0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0012R\u0019\u0010\'\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0012R\u000e\u0010)\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006X"}, d2 = {"Lcom/benhe/fitlog/viewmodel/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "KEY_CUSTOM_FOODS", "", "PREFS_NAME", "_customFoodItems", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/benhe/fitlog/model/FoodItem;", "activityDao", "Lcom/benhe/fitlog/data/local/dao/DailyActivityDao;", "allFoodCategories", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/benhe/fitlog/model/FoodCategory;", "getAllFoodCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "bodyStatDao", "Lcom/benhe/fitlog/data/local/dao/BodyStatDao;", "bodyStatHistory", "Lcom/benhe/fitlog/model/BodyStatRecord;", "getBodyStatHistory", "bodyStatus", "", "Lcom/benhe/fitlog/model/BodyRegion;", "", "getBodyStatus", "dailyActivityDao", "dao", "db", "Lcom/benhe/fitlog/data/local/AppDatabase;", "dietDao", "Lcom/benhe/fitlog/data/local/dao/DietDao;", "gson", "Lcom/google/gson/Gson;", "isAfterburnAutoActive", "", "latestBodyStat", "getLatestBodyStat", "selectedDateString", "workoutDao", "Lcom/benhe/fitlog/data/local/dao/WorkoutDao;", "workoutRepository", "Lcom/benhe/fitlog/data/repository/WorkoutRepository;", "addCustomFood", "", "item", "deleteDietRecord", "record", "Lcom/benhe/fitlog/data/local/entiy/DietRecord;", "getActivityForDate", "Lkotlinx/coroutines/flow/Flow;", "Lcom/benhe/fitlog/model/DailyActivity;", "date", "getDietRecordsForDate", "getSetsByDate", "Lcom/benhe/fitlog/data/local/entiy/WorkoutSet;", "getTodayExpenditure", "", "activity", "getTotalCaloriesForDate", "", "getTotalCarbsForDate", "getTotalProteinForDate", "getUserProfile", "Lcom/benhe/fitlog/model/UserProfile;", "loadCustomFoods", "onActivityConfirm", "sleep", "intensity", "Lcom/benhe/fitlog/model/LifeIntensity;", "saveBodyStat", "weight", "bodyFat", "saveDietRecord", "foodName", "category", "quantity", "calories", "protein", "carbs", "syncWorkoutSets", "dateString", "drafts", "Lkotlin/Pair;", "updateActivityForDate", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.AppDatabase db = null;
    
    /**
     * 饮食记录 DAO
     */
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.DietDao dietDao = null;
    
    /**
     * 每日活动/睡眠/强度 DAO
     */
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.DailyActivityDao dailyActivityDao = null;
    
    /**
     * 训练记录 DAO
     */
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.WorkoutDao workoutDao = null;
    
    /**
     * 身体成分（体重/体脂） DAO
     */
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.BodyStatDao bodyStatDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.BodyStatDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.DailyActivityDao activityDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.repository.WorkoutRepository workoutRepository = null;
    
    /**
     * 1. 实时监听最新的身体指标数据。
     * 用于 UI 侧边栏显示当前的体重和体脂率。
     * 如果没有数据，初始值为 null。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.model.BodyStatRecord> latestBodyStat = null;
    
    /**
     * 2. 实时监听身体指标历史列表。
     * 用于 UI 左侧的折线图展示历史趋势。
     * 如果没有数据，初始值为空列表。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.BodyStatRecord>> bodyStatHistory = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String PREFS_NAME = "user_prefs";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_CUSTOM_FOODS = "custom_foods_list";
    
    /**
     * 内部持有的自定义食物列表状态流
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.benhe.fitlog.model.FoodItem>> _customFoodItems = null;
    
    /**
     * 3. 【核心 UI 数据源】对外暴露的完整食物分类列表流。
     *
     * 它的作用是将硬编码的默认食物库 (`FoodCatalog.categories`) 与用户添加的自定义食物 (`_customFoodItems`) 进行合并。
     * 合并后的列表可以直接供 UI 的 ExpandableListView 使用。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.FoodCategory>> allFoodCategories = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectedDateString = null;
    
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
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> bodyStatus = null;
    
    /**
     * 自动化后燃状态流。
     *
     * 基于 `bodyStatus` 的计算结果衍生而来。如果当前任意一个身体部位的恢复系数低于 0.5（例如深度疲劳），
     * 则自动判定为处于“后燃效应”活跃状态 (true)，否则为 false。
     * 这解决了手动设置后燃开关容易遗忘的问题。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAfterburnAutoActive = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    /**
     * 1. 实时监听最新的身体指标数据。
     * 用于 UI 侧边栏显示当前的体重和体脂率。
     * 如果没有数据，初始值为 null。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.model.BodyStatRecord> getLatestBodyStat() {
        return null;
    }
    
    /**
     * 2. 实时监听身体指标历史列表。
     * 用于 UI 左侧的折线图展示历史趋势。
     * 如果没有数据，初始值为空列表。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.BodyStatRecord>> getBodyStatHistory() {
        return null;
    }
    
    /**
     * 3. 【核心 UI 数据源】对外暴露的完整食物分类列表流。
     *
     * 它的作用是将硬编码的默认食物库 (`FoodCatalog.categories`) 与用户添加的自定义食物 (`_customFoodItems`) 进行合并。
     * 合并后的列表可以直接供 UI 的 ExpandableListView 使用。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.FoodCategory>> getAllFoodCategories() {
        return null;
    }
    
    /**
     * 从 SharedPreferences 中异步加载用户保存的自定义食物列表。
     * 加载成功后更新 `_customFoodItems` 状态流。
     */
    private final void loadCustomFoods() {
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
    public final void addCustomFood(@org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.FoodItem item) {
    }
    
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
    public final void syncWorkoutSets(@org.jetbrains.annotations.NotNull()
    java.lang.String dateString, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.benhe.fitlog.model.BodyRegion, kotlin.Pair<java.lang.Integer, java.lang.String>> drafts) {
    }
    
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
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> getBodyStatus() {
        return null;
    }
    
    /**
     * 自动化后燃状态流。
     *
     * 基于 `bodyStatus` 的计算结果衍生而来。如果当前任意一个身体部位的恢复系数低于 0.5（例如深度疲劳），
     * 则自动判定为处于“后燃效应”活跃状态 (true)，否则为 false。
     * 这解决了手动设置后燃开关容易遗忘的问题。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAfterburnAutoActive() {
        return null;
    }
    
    /**
     * 从 SharedPreferences 中读取用户的基础资料（体重、身高、年龄、性别）。
     * 包含对体重和身高单位的自动纠错逻辑（防止用户错误输入克或毫米）。
     * @return UserProfile 对象
     */
    private final com.benhe.fitlog.model.UserProfile getUserProfile() {
        return null;
    }
    
    /**
     * 计算今日的总热量消耗 (TDEE)。
     *
     * 基于用户的基础代谢率 (BMR)，结合当天的生活强度系数和后燃状态进行计算。
     *
     * @param activity 当天的活动记录对象。如果为空，则使用默认强度，并使用自动计算的后燃状态 (`isAfterburnAutoActive`)。
     * @return 估算的每日总消耗热量 (Kcal)
     */
    public final int getTodayExpenditure(@org.jetbrains.annotations.Nullable()
    com.benhe.fitlog.model.DailyActivity activity) {
        return 0;
    }
    
    /**
     * 当用户在 UI 上确认修改了睡眠和强度时调用此方法。
     * 它会使用当前自动计算的后燃状态 (`isAfterburnAutoActive`) 来更新数据库。
     */
    public final void onActivityConfirm(@org.jetbrains.annotations.NotNull()
    java.lang.String date, float sleep, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity intensity) {
    }
    
    /**
     * 内部方法：更新指定日期的每日状态记录。
     * 使用自动计算的后燃状态值。
     */
    private final void updateActivityForDate(java.lang.String date, float sleep, com.benhe.fitlog.model.LifeIntensity intensity) {
    }
    
    /**
     * 获取指定日期的每日活动状态流。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.benhe.fitlog.model.DailyActivity> getActivityForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
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
    public final void saveDietRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String foodName, @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    java.lang.String quantity, double calories, double protein, double carbs, @org.jetbrains.annotations.NotNull()
    java.lang.String date) {
    }
    
    /**
     * 获取指定日期的总摄入热量流
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getTotalCaloriesForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    /**
     * 获取指定日期的总摄入蛋白质流
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getTotalProteinForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    /**
     * 获取指定日期的总摄入碳水流
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getTotalCarbsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    /**
     * 获取指定日期的所有饮食记录列表流
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.benhe.fitlog.data.local.entiy.DietRecord>> getDietRecordsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    /**
     * 删除一条指定的饮食记录
     */
    public final void deleteDietRecord(@org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.data.local.entiy.DietRecord record) {
    }
    
    /**
     * 获取指定日期范围内的所有训练子记录 (WorkoutSet)。
     * 通过计算该日期的起始和结束时间戳来进行范围查询。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.benhe.fitlog.data.local.entiy.WorkoutSet>> getSetsByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    /**
     * 保存当前的体重和体脂率数据。
     *
     * 策略：为了数据整洁和图表展示方便，会将时间戳对齐到当前的**整点**（例如 14:35 保存，实际记录时间为 14:00:00）。
     *
     * @param weight 体重 (kg)
     * @param bodyFat 体脂率 (%)
     */
    public final void saveBodyStat(float weight, float bodyFat) {
    }
}