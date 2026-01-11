// core/domain/DayRecord.js

/**
 * 一天的聚合数据（Calendar 的唯一数据源）
 */
export function createEmptyDayRecord(date) {
  return {
    date,                 // 'YYYY-MM-DD'
    nutrition: null,      // { totalKcal, totalProtein }
    training: null,       // 预留：训练汇总
    meta: {
      hasData: false,     // 该天是否有任何记录
      updatedAt: null,   // 最后更新时间（可选，但很有用）
    },
  };
}
