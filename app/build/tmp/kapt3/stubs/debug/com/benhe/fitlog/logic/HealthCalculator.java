package com.benhe.fitlog.logic;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J7\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010\u00a8\u0006\u0014"}, d2 = {"Lcom/benhe/fitlog/logic/HealthCalculator;", "", "()V", "calcBMR", "", "weight", "", "height", "age", "gender", "", "bodyFat", "(DDILjava/lang/String;Ljava/lang/Double;)I", "calculateDailyExpenditure", "bmr", "intensity", "Lcom/benhe/fitlog/model/LifeIntensity;", "calculateRecoveryFactor", "", "sleepHours", "app_debug"})
public final class HealthCalculator {
    @org.jetbrains.annotations.NotNull()
    public static final com.benhe.fitlog.logic.HealthCalculator INSTANCE = null;
    
    private HealthCalculator() {
        super();
    }
    
    /**
     * 1. 计算基础代谢 BMR (基础出厂设置)
     * 使用 Mifflin-St Jeor 公式
     */
    public final int calcBMR(double weight, double height, int age, @org.jetbrains.annotations.NotNull()
    java.lang.String gender, @org.jetbrains.annotations.Nullable()
    java.lang.Double bodyFat) {
        return 0;
    }
    
    /**
     * 2. 计算每日总能量消耗 (TDEE)
     * 逻辑：基础代谢 * 生活强度系数
     */
    public final int calculateDailyExpenditure(int bmr, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity intensity) {
        return 0;
    }
    
    /**
     * 3. 计算综合恢复系数 (用于训练模块)
     * 逻辑：睡眠影响 * 强度消耗影响
     */
    public final float calculateRecoveryFactor(float sleepHours, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity intensity) {
        return 0.0F;
    }
}