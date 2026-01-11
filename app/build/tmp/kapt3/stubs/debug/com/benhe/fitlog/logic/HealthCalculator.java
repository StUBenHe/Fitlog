package com.benhe.fitlog.logic;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J7\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/benhe/fitlog/logic/HealthCalculator;", "", "()V", "calcBMR", "", "weight", "", "height", "age", "gender", "", "bodyFat", "(DDILjava/lang/String;Ljava/lang/Double;)I", "app_debug"})
public final class HealthCalculator {
    @org.jetbrains.annotations.NotNull()
    public static final com.benhe.fitlog.logic.HealthCalculator INSTANCE = null;
    
    private HealthCalculator() {
        super();
    }
    
    /**
     * 计算基础代谢 BMR
     */
    public final int calcBMR(double weight, double height, int age, @org.jetbrains.annotations.NotNull()
    java.lang.String gender, @org.jetbrains.annotations.Nullable()
    java.lang.Double bodyFat) {
        return 0;
    }
}