package com.benhe.fitlog.viewmodel;

/**
 * 应用的核心 ViewModel。
 * 负责管理主界面的 UI 状态，处理用户交互，并作为 UI 层与数据层之间的桥梁。
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ec\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020\rJ\u001c\u0010F\u001a\b\u0012\u0004\u0012\u0002000/2\f\u0010G\u001a\b\u0012\u0004\u0012\u00020H0\fH\u0002J\u0006\u0010I\u001a\u00020DJ\u000e\u0010J\u001a\u00020D2\u0006\u0010K\u001a\u00020LJ\u0016\u0010M\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010O0N2\u0006\u0010P\u001a\u00020\u0006J\u001a\u0010Q\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020L0\f0N2\u0006\u0010P\u001a\u00020\u0006J\u001a\u0010R\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020S0\f0N2\u0006\u0010P\u001a\u00020\u0006J\u0010\u0010T\u001a\u0002092\b\u0010U\u001a\u0004\u0018\u00010OJ\u0014\u0010V\u001a\b\u0012\u0004\u0012\u00020W0N2\u0006\u0010P\u001a\u00020\u0006J\u0014\u0010X\u001a\b\u0012\u0004\u0012\u00020W0N2\u0006\u0010P\u001a\u00020\u0006J\u0014\u0010Y\u001a\b\u0012\u0004\u0012\u00020W0N2\u0006\u0010P\u001a\u00020\u0006J\b\u0010Z\u001a\u00020[H\u0002J\b\u0010\\\u001a\u00020DH\u0002J\u000e\u0010]\u001a\u00020D2\u0006\u0010^\u001a\u00020\u0006J\u001e\u0010_\u001a\u00020D2\u0006\u0010P\u001a\u00020\u00062\u0006\u0010`\u001a\u00020 2\u0006\u0010a\u001a\u00020bJ\u0016\u0010c\u001a\u00020D2\u0006\u0010d\u001a\u00020 2\u0006\u0010e\u001a\u00020 J>\u0010f\u001a\u00020D2\u0006\u0010g\u001a\u00020\u00062\u0006\u0010h\u001a\u00020\u00062\u0006\u0010i\u001a\u00020\u00062\u0006\u0010j\u001a\u00020W2\u0006\u0010k\u001a\u00020W2\u0006\u0010l\u001a\u00020W2\u0006\u0010P\u001a\u00020\u0006J.\u0010m\u001a\u00020D2\u0006\u0010^\u001a\u00020\u00062\u001e\u0010n\u001a\u001a\u0012\u0004\u0012\u00020\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u000209\u0012\u0004\u0012\u00020\u0006080\u001eJ\u000e\u0010o\u001a\u00020D2\u0006\u0010P\u001a\u000200J \u0010p\u001a\u00020D2\u0006\u0010P\u001a\u00020\u00062\u0006\u0010`\u001a\u00020 2\u0006\u0010a\u001a\u00020bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\f0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\n0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\f0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0015R#\u0010\u001d\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020 0\u001e0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0015R\u000e\u0010\"\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010*\u001a\b\u0012\u0004\u0012\u00020+0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0015R\u0019\u0010,\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0015R\u001d\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002000/0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u0015R\u000e\u00102\u001a\u000203X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002000/0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u0015R)\u00106\u001a\u001a\u0012\u0004\u0012\u00020\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u000209\u0012\u0004\u0012\u00020\u00060807\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010;R\u000e\u0010<\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010=\u001a\b\u0012\u0004\u0012\u00020\n0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010\u0015R\u000e\u0010?\u001a\u00020@X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020BX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006q"}, d2 = {"Lcom/benhe/fitlog/viewmodel/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "KEY_CUSTOM_FOODS", "", "PREFS_NAME", "_bodyFatSummary", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/benhe/fitlog/util/BodyStatsSummary;", "_customFoodItems", "", "Lcom/benhe/fitlog/model/FoodItem;", "_weightSummary", "activityDao", "Lcom/benhe/fitlog/data/local/dao/DailyActivityDao;", "allFoodCategories", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/benhe/fitlog/model/FoodCategory;", "getAllFoodCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "bodyFatSummary", "getBodyFatSummary", "bodyStatDao", "Lcom/benhe/fitlog/data/local/dao/BodyStatDao;", "bodyStatHistory", "Lcom/benhe/fitlog/model/BodyStatRecord;", "getBodyStatHistory", "bodyStatus", "", "Lcom/benhe/fitlog/model/BodyRegion;", "", "getBodyStatus", "dailyActivityDao", "dao", "db", "Lcom/benhe/fitlog/data/local/AppDatabase;", "dietDao", "Lcom/benhe/fitlog/data/local/dao/DietDao;", "gson", "Lcom/google/gson/Gson;", "isAfterburnAutoActive", "", "latestBodyStat", "getLatestBodyStat", "pastPeriodDates", "", "Ljava/time/LocalDate;", "getPastPeriodDates", "periodDao", "Lcom/benhe/fitlog/data/local/dao/PeriodDao;", "predictedPeriodDates", "getPredictedPeriodDates", "regionDraftState", "Landroidx/compose/runtime/snapshots/SnapshotStateMap;", "Lkotlin/Pair;", "", "getRegionDraftState", "()Landroidx/compose/runtime/snapshots/SnapshotStateMap;", "selectedDateString", "weightSummary", "getWeightSummary", "workoutDao", "Lcom/benhe/fitlog/data/local/dao/WorkoutDao;", "workoutRepository", "Lcom/benhe/fitlog/data/repository/WorkoutRepository;", "addCustomFood", "", "item", "calculatePrediction", "history", "Lcom/benhe/fitlog/model/PeriodDay;", "clearWorkoutDrafts", "deleteDietRecord", "record", "Lcom/benhe/fitlog/data/local/entiy/DietRecord;", "getActivityForDate", "Lkotlinx/coroutines/flow/Flow;", "Lcom/benhe/fitlog/model/DailyActivity;", "date", "getDietRecordsForDate", "getSetsByDate", "Lcom/benhe/fitlog/data/local/entiy/WorkoutSet;", "getTodayExpenditure", "activity", "getTotalCaloriesForDate", "", "getTotalCarbsForDate", "getTotalProteinForDate", "getUserProfile", "Lcom/benhe/fitlog/model/UserProfile;", "loadCustomFoods", "loadWorkoutDraftsForDate", "dateString", "onActivityConfirm", "sleep", "intensity", "Lcom/benhe/fitlog/model/LifeIntensity;", "saveBodyStat", "weight", "bodyFat", "saveDietRecord", "foodName", "category", "quantity", "calories", "protein", "carbs", "syncWorkoutSets", "drafts", "togglePeriodStatus", "updateActivityForDate", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.DietDao dietDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.DailyActivityDao dailyActivityDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.WorkoutDao workoutDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.BodyStatDao bodyStatDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.PeriodDao periodDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.BodyStatDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.local.dao.DailyActivityDao activityDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.repository.WorkoutRepository workoutRepository = null;
    
    /**
     * 保存身体部位的编辑状态 (Key: 部位, Value: Pair<等级, 备注>)
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.snapshots.SnapshotStateMap<com.benhe.fitlog.model.BodyRegion, kotlin.Pair<java.lang.Integer, java.lang.String>> regionDraftState = null;
    
    /**
     * 实时监听最新的身体指标数据 (侧边栏使用)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.model.BodyStatRecord> latestBodyStat = null;
    
    /**
     * 实时监听身体指标历史列表 (左侧折线图使用)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.BodyStatRecord>> bodyStatHistory = null;
    
    /**
     * 获取所有历史经期日期集合 (深色标记)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.time.LocalDate>> pastPeriodDates = null;
    
    /**
     * 获取预测的未来经期日期集合 (浅色标记)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.time.LocalDate>> predictedPeriodDates = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String PREFS_NAME = "user_prefs";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String KEY_CUSTOM_FOODS = "custom_foods_list";
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.benhe.fitlog.model.FoodItem>> _customFoodItems = null;
    
    /**
     * 完整食物分类列表 (默认+自定义)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.FoodCategory>> allFoodCategories = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectedDateString = null;
    
    /**
     * 身体各部位恢复状态流 (0.0 - 1.0)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> bodyStatus = null;
    
    /**
     * 自动化后燃状态流 (基于 bodyStatus)
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAfterburnAutoActive = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.benhe.fitlog.util.BodyStatsSummary> _weightSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.util.BodyStatsSummary> weightSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.benhe.fitlog.util.BodyStatsSummary> _bodyFatSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.util.BodyStatsSummary> bodyFatSummary = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    /**
     * 保存身体部位的编辑状态 (Key: 部位, Value: Pair<等级, 备注>)
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.runtime.snapshots.SnapshotStateMap<com.benhe.fitlog.model.BodyRegion, kotlin.Pair<java.lang.Integer, java.lang.String>> getRegionDraftState() {
        return null;
    }
    
    /**
     * 清空所有训练记录草稿
     */
    public final void clearWorkoutDrafts() {
    }
    
    /**
     * 实时监听最新的身体指标数据 (侧边栏使用)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.model.BodyStatRecord> getLatestBodyStat() {
        return null;
    }
    
    /**
     * 实时监听身体指标历史列表 (左侧折线图使用)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.BodyStatRecord>> getBodyStatHistory() {
        return null;
    }
    
    /**
     * 获取所有历史经期日期集合 (深色标记)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.time.LocalDate>> getPastPeriodDates() {
        return null;
    }
    
    /**
     * 获取预测的未来经期日期集合 (浅色标记)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.time.LocalDate>> getPredictedPeriodDates() {
        return null;
    }
    
    /**
     * 经期预测算法 (基础版: 周期28天, 持续5天)
     */
    private final java.util.Set<java.time.LocalDate> calculatePrediction(java.util.List<com.benhe.fitlog.model.PeriodDay> history) {
        return null;
    }
    
    /**
     * 切换指定日期的经期状态
     */
    public final void togglePeriodStatus(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
    }
    
    /**
     * 完整食物分类列表 (默认+自定义)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.benhe.fitlog.model.FoodCategory>> getAllFoodCategories() {
        return null;
    }
    
    private final void loadCustomFoods() {
    }
    
    public final void addCustomFood(@org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.FoodItem item) {
    }
    
    /**
     * 【核心修复后的方法】同步保存某一天的所有部位训练摘要记录。
     * 采用策略：**“覆盖更新”** (Replace by Day)。
     *
     * @param dateString 目标日期字符串 (yyyy-MM-dd)
     * @param drafts 包含部位数据的 Map (UI 状态 regionDraftState)
     * // 修改：移除了 customItems 参数
     */
    public final void syncWorkoutSets(@org.jetbrains.annotations.NotNull()
    java.lang.String dateString, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.benhe.fitlog.model.BodyRegion, kotlin.Pair<java.lang.Integer, java.lang.String>> drafts) {
    }
    
    /**
     * 身体各部位恢复状态流 (0.0 - 1.0)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> getBodyStatus() {
        return null;
    }
    
    /**
     * 自动化后燃状态流 (基于 bodyStatus)
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAfterburnAutoActive() {
        return null;
    }
    
    private final com.benhe.fitlog.model.UserProfile getUserProfile() {
        return null;
    }
    
    /**
     * 计算今日的总热量消耗 (TDEE)
     */
    public final int getTodayExpenditure(@org.jetbrains.annotations.Nullable()
    com.benhe.fitlog.model.DailyActivity activity) {
        return 0;
    }
    
    /**
     * 确认修改每日活动状态
     */
    public final void onActivityConfirm(@org.jetbrains.annotations.NotNull()
    java.lang.String date, float sleep, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity intensity) {
    }
    
    private final void updateActivityForDate(java.lang.String date, float sleep, com.benhe.fitlog.model.LifeIntensity intensity) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.benhe.fitlog.model.DailyActivity> getActivityForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    public final void saveDietRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String foodName, @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    java.lang.String quantity, double calories, double protein, double carbs, @org.jetbrains.annotations.NotNull()
    java.lang.String date) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getTotalCaloriesForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getTotalProteinForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getTotalCarbsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.benhe.fitlog.data.local.entiy.DietRecord>> getDietRecordsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    public final void deleteDietRecord(@org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.data.local.entiy.DietRecord record) {
    }
    
    /**
     * 获取指定日期范围内的所有训练子记录
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.benhe.fitlog.data.local.entiy.WorkoutSet>> getSetsByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    public final void saveBodyStat(float weight, float bodyFat) {
    }
    
    public final void loadWorkoutDraftsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String dateString) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.util.BodyStatsSummary> getWeightSummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.benhe.fitlog.util.BodyStatsSummary> getBodyFatSummary() {
        return null;
    }
}