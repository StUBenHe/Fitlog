package com.benhe.fitlog.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0098\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u0016\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ\u001a\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190 0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ\u001a\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0 0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ\u0010\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u001cJ\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ\u0014\u0010(\u001a\b\u0012\u0004\u0012\u00020\'0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ\u0014\u0010)\u001a\b\u0012\u0004\u0012\u00020\'0\u001b2\u0006\u0010\u001d\u001a\u00020\u001eJ\b\u0010*\u001a\u00020+H\u0002J>\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u001e2\u0006\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u00020\u001e2\u0006\u00100\u001a\u00020\'2\u0006\u00101\u001a\u00020\'2\u0006\u00102\u001a\u00020\'2\u0006\u0010\u001d\u001a\u00020\u001eJ6\u00103\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u00104\u001a\u00020\b2\u0006\u00105\u001a\u00020\t2\u0006\u00106\u001a\u00020$2\u0006\u00107\u001a\u00020$2\u0006\u00108\u001a\u00020\u001eJ.\u00109\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001e2\u001e\u0010:\u001a\u001a\u0012\u0004\u0012\u00020\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u001e0;0\u0007J&\u0010<\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010=\u001a\u00020\t2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AR#\u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006B"}, d2 = {"Lcom/benhe/fitlog/viewmodel/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "bodyStatus", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/benhe/fitlog/model/BodyRegion;", "", "getBodyStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "dailyActivityDao", "Lcom/benhe/fitlog/data/DailyActivityDao;", "db", "Lcom/benhe/fitlog/data/db/AppDatabase;", "dietDao", "Lcom/benhe/fitlog/data/db/DietDao;", "workoutDao", "Lcom/benhe/fitlog/data/dao/WorkoutDao;", "workoutRepository", "Lcom/benhe/fitlog/data/repository/WorkoutRepository;", "deleteDietRecord", "", "record", "Lcom/benhe/fitlog/data/db/DietRecord;", "getActivityForDate", "Lkotlinx/coroutines/flow/Flow;", "Lcom/benhe/fitlog/model/DailyActivity;", "date", "", "getDietRecordsForDate", "", "getSetsByDate", "Lcom/benhe/fitlog/data/entity/WorkoutSet;", "getTodayExpenditure", "", "activity", "getTotalCaloriesForDate", "", "getTotalCarbsForDate", "getTotalProteinForDate", "getUserProfile", "Lcom/benhe/fitlog/model/UserProfile;", "saveDietRecord", "foodName", "category", "quantity", "calories", "protein", "carbs", "saveWorkoutSet", "region", "weight", "reps", "rpe", "note", "syncWorkoutSets", "records", "Lkotlin/Pair;", "updateActivityForDate", "sleep", "intensity", "Lcom/benhe/fitlog/model/LifeIntensity;", "afterburn", "", "app_debug"})
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
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> bodyStatus = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    /**
     * 2. 获取 8 大区块负荷状态
     * 解决 Cannot infer type 的关键：显式指定 emptyMap 的类型
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<com.benhe.fitlog.model.BodyRegion, java.lang.Float>> getBodyStatus() {
        return null;
    }
    
    /**
     * 3. 提取用户资料 (用于计算)
     */
    private final com.benhe.fitlog.model.UserProfile getUserProfile() {
        return null;
    }
    
    /**
     * 4. 计算今日消耗 (由 UI 调用)
     */
    public final int getTodayExpenditure(@org.jetbrains.annotations.Nullable()
    com.benhe.fitlog.model.DailyActivity activity) {
        return 0;
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
}