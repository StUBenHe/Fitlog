package com.benhe.fitlog;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aN\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u001e\u0010\n\u001a\u001a\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u001a2\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a6\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001aR\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u00112\u0018\u0010\u001b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u001d0\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006#"}, d2 = {"ActivityInputDialog", "", "initialSleep", "", "initialIntensity", "Lcom/benhe/fitlog/model/LifeIntensity;", "initialAfterburn", "", "onDismiss", "Lkotlin/Function0;", "onConfirm", "Lkotlin/Function3;", "CalendarScreen", "viewModel", "Lcom/benhe/fitlog/viewmodel/MainViewModel;", "onNavigateToDiet", "Lkotlin/Function1;", "", "onEditProfile", "DayCard", "date", "weekday", "isToday", "onDietClick", "ExpandedModuleItem", "title", "mainValue", "subItems", "", "Lkotlin/Pair;", "color", "Landroidx/compose/ui/graphics/Color;", "onClick", "ExpandedModuleItem-42QJj7c", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;JLkotlin/jvm/functions/Function0;)V", "app_debug"})
public final class MainActivityKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    public static final void CalendarScreen(@org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.viewmodel.MainViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNavigateToDiet, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onEditProfile) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DayCard(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String weekday, boolean isToday, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.viewmodel.MainViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDietClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ActivityInputDialog(float initialSleep, @org.jetbrains.annotations.NotNull()
    com.benhe.fitlog.model.LifeIntensity initialIntensity, boolean initialAfterburn, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super java.lang.Float, ? super com.benhe.fitlog.model.LifeIntensity, ? super java.lang.Boolean, kotlin.Unit> onConfirm) {
    }
}