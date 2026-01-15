package com.benhe.fitlog.data.repository

import com.benhe.fitlog.data.local.dao.WorkoutDao
import com.benhe.fitlog.data.local.entiy.WorkoutSet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

/**
 * 训练数据仓库 (Repository)。
 *
 * Repository 是连接 ViewModel 和数据源 (DAO) 的桥梁。
 * 它的职责是向 ViewModel 提供清晰、干净的数据 API，并处理具体的数据获取策略（例如，是从本地数据库读还是从网络取，数据需要做什么预处理等）。
 * ViewModel 不需要知道数据是从哪里来的，只需要调用 Repository 的方法即可。
 *
 * @property dao 训练相关的数据库访问对象，由依赖注入框架（如 Hilt）或手动方式注入。
 */
class WorkoutRepository(private val dao: WorkoutDao) {

    /**
     * 获取最近 14 天（两周）内的所有训练组记录。
     *
     * 这个方法封装了具体的日期计算逻辑，向调用者提供一个直接可用的数据流。
     * 它通常用于在首页或统计页面展示近期的训练概况。
     *
     * @return 发出符合时间范围的训练组列表 (List<WorkoutSet>) 的 Flow。
     *         Flow 是冷流，只有在被收集 (collect) 时才会开始执行数据库查询，并且当数据库数据发生变化时，会自动发出最新的列表。
     */
    fun getRecentSetsFlow(): Flow<List<WorkoutSet>> {
        // --- 1. 计算时间范围 ---

        // 计算起始时间戳：当前日期的前 14 天
        val startMillis = LocalDate.now()
            .minusDays(14) // 往前推 14 天
            // 将日期转换为当天的起始时间 (00:00:00)，使用系统默认时区
            .atStartOfDay(ZoneId.systemDefault())
            // 转换为 Instant 时间点
            .toInstant()
            // 获取毫秒数时间戳
            .toEpochMilli()

        // 计算结束时间戳：当前时间往后推一天
        // 这里加一天是为了确保当前时刻之后发生的数据（如果用户手动修改了时间或有未来数据）也能被包含进来，
        // 作为一个安全边界。86400000L 是一天的毫秒数。
        val futureMillis = System.currentTimeMillis() + 86400000L

        // --- 2. 调用 DAO ---
        // 将计算好的起始和结束时间戳传递给 DAO 的查询方法。
        return dao.getSetsInDateRange(startMillis, futureMillis)
    }
}