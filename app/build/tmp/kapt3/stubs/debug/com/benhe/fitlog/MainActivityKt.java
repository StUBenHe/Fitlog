package com.benhe.fitlog;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a2\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001a6\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001aR\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00062\u0018\u0010\u0012\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u00140\u00132\u0006\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0019\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001a"}, d2 = {"CalendarScreen", "", "viewModel", "Lcom/benhe/fitlog/viewmodel/MainViewModel;", "onNavigateToDiet", "Lkotlin/Function1;", "", "onEditProfile", "Lkotlin/Function0;", "DayCard", "date", "weekday", "isToday", "", "onDietClick", "ExpandedModuleItem", "title", "mainValue", "subItems", "", "Lkotlin/Pair;", "color", "Landroidx/compose/ui/graphics/Color;", "onClick", "ExpandedModuleItem-42QJj7c", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;JLkotlin/jvm/functions/Function0;)V", "app_debug"})
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
}