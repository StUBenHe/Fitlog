package com.benhe.fitlog.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(J\u0016\u0010)\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\u0006\u0010*\u001a\u00020\u001cJ\u001a\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0,0\u00112\u0006\u0010*\u001a\u00020\u001cJ\u001a\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020.0,0\u00112\u0006\u0010*\u001a\u00020\u001cJ\u0010\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u00010\u0012J\u0014\u00102\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00112\u0006\u0010*\u001a\u00020\u001cJ\u0014\u00103\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00112\u0006\u0010*\u001a\u00020\u001cJ\u0014\u00104\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00112\u0006\u0010*\u001a\u00020\u001cJ\b\u00105\u001a\u000206H\u0002J\u001e\u00107\u001a\u00020&2\u0006\u0010*\u001a\u00020\u001c2\u0006\u00108\u001a\u00020\t2\u0006\u00109\u001a\u00020:J>\u0010;\u001a\u00020&2\u0006\u0010<\u001a\u00020\u001c2\u0006\u0010=\u001a\u00020\u001c2\u0006\u0010>\u001a\u00020\u001c2\u0006\u0010?\u001a\u00020\u001f2\u0006\u0010@\u001a\u00020\u001f2\u0006\u0010A\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020\u001cJ6\u0010B\u001a\u00020&2\u0006\u0010*\u001a\u00020\u001c2\u0006\u0010C\u001a\u00020\b2\u0006\u0010D\u001a\u00020\t2\u0006\u0010E\u001a\u0002002\u0006\u0010F\u001a\u0002002\u0006\u0010G\u001a\u00020\u001cJ.\u0010H\u001a\u00020&2\u0006\u0010*\u001a\u00020\u001c2\u001e\u0010I\u001a\u001a\u0012\u0004\u0012\u00020\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u000200\u0012\u0004\u0012\u00020\u001c0J0\u0007J\u001e\u0010K\u001a\u00020&2\u0006\u0010*\u001a\u00020\u001c2\u0006\u00108\u001a\u00020\t2\u0006\u00109\u001a\u00020:J&\u0010K\u001a\u00020&2\u0006\u0010*\u001a\u00020\u001c2\u0006\u00108\u001a\u00020\t2\u0006\u00109\u001a\u00020:2\u0006\u0010L\u001a\u00020\u001aR#\u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000bR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0014R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006M"}, d2 = {"Lcom/benhe/fitlog/viewmodel/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "bodyStatus", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/benhe/fitlog/model/BodyRegion;", "", "getBodyStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "currentProtein", "currentSleepHours", "dailyActivityDao", "Lcom/benhe/fitlog/data/DailyActivityDao;", "dailyActivityFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/benhe/fitlog/model/DailyActivity;", "getDailyActivityFlow", "()Lkotlinx/coroutines/flow/Flow;", "db", "Lcom/benhe/fitlog/data/db/AppDatabase;", "dietDao", "Lcom/benhe/fitlog/data/db/DietDao;", "isAfterburnAutoActive", "", "selectedDate", "", "selectedDateString", "totalProteinFlow", "", "getTotalProteinFlow", "workoutDao", "Lcom/benhe/fitlog/data/dao/WorkoutDao;", "workoutRepository", "Lcom/benhe/fitlog/data/repository/WorkoutRepository;", "deleteDietRecord", "", "record", "Lcom/benhe/fitlog/data/db/DietRecord;", "getActivityForDate", "date", "getDietRecordsForDate", "", "getSetsByDate", "Lcom/benhe/fitlog/data/entity/WorkoutSet;", "getTodayExpenditure", "", "activity", "getTotalCaloriesForDate", "getTotalCarbsForDate", "getTotalProteinForDate", "getUserProfile", "Lcom/benhe/fitlog/model/UserProfile;", "onActivityConfirm", "sleep", "intensity", "Lcom/benhe/fitlog/model/LifeIntensity;", "saveDietRecord", "foodName", "category", "quantity", "calories", "protein", "carbs", "saveWorkoutSet", "region", "weight", "reps", "rpe", "note", "syncWorkoutSets", "records", "Lkotlin/Pair;", "updateActivityForDate", "afterburn", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.db.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.db.DietDao dietDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.DailyActivityDao dailyActivityDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.dao.WorkoutDao workoutDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.repository.WorkoutRepository workoutRepository = null;
    
    /**
     * 2. 获取 8 大区块负荷状态
     * 解决 Cannot infer type 的关键：显式指定 emptyMap 的类型
     */
    private final float currentSleepHours = 8.0F;
    private final float currentProtein = 100.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectedDate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.benhe.fitlog.model.DailyActivity> dailyActivityFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Double> totalProteinFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectedDateString = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> bodyStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAfterburnAutoActive = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.benhe.fitlog.model.DailyActivity> getDailyActivityFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Double> getTotalProteinFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> getBodyStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isAfterburnAutoActive() {
        return null;
    }
    
    /**
     * 计算今日消耗 (修正参数报错)
     */
    public final int getTodayExpenditure(@org.jetbrains.annotations.Nullable()
    com.benhe.fitlog.model.DailyActivity activity) {
        return 0;
    }
    
    /**
     * 更新每日状态 (确认时调用)
     */
    public final void updateActivityForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date, float sleep, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity intensity) {
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
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.benhe.fitlog.data.db.DietRecord>> getDietRecordsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    public final void deleteDietRecord(@org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.data.db.DietRecord record) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.benhe.fitlog.model.DailyActivity> getActivityForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    public final void updateActivityForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date, float sleep, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity intensity, boolean afterburn) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.benhe.fitlog.data.entity.WorkoutSet>> getSetsByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    public final void saveWorkoutSet(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.BodyRegion region, float weight, int reps, int rpe, @org.jetbrains.annotations.NotNull()
    java.lang.String note) {
    }
    
    public final void syncWorkoutSets(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.benhe.fitlog.model.BodyRegion, kotlin.Pair<java.lang.Integer, java.lang.String>> records) {
    }
    
    private final com.benhe.fitlog.model.UserProfile getUserProfile() {
        return null;
    }
    
    public final void onActivityConfirm(@org.jetbrains.annotations.NotNull()
    java.lang.String date, float sleep, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity intensity) {
    }
}