package com.benhe.fitlog.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\b2\u0006\u0010\u000b\u001a\u00020\fJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\b2\u0006\u0010\u000b\u001a\u00020\fJ6\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/benhe/fitlog/viewmodel/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "dietDao", "Lcom/benhe/fitlog/data/db/DietDao;", "getDietRecordsForDate", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/benhe/fitlog/data/db/DietRecord;", "date", "", "getTotalCaloriesForDate", "", "getTotalProteinForDate", "saveDietRecord", "", "foodName", "category", "quantity", "calories", "protein", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.benhe.fitlog.data.db.DietDao dietDao = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    public final void saveDietRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String foodName, @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    java.lang.String quantity, double calories, double protein, @org.jetbrains.annotations.NotNull()
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
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.benhe.fitlog.data.db.DietRecord>> getDietRecordsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
}