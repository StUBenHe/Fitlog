// core/repositories/dayRepository.js
import { createEmptyDayRecord } from "../domain/DayRecord";

const KEY_PREFIX = "day_record_";

function keyOf(date) {
  return KEY_PREFIX + date;
}

/**
 * 读取某一天的数据
 * - 如果不存在：自动创建一个空 DayRecord
 */
export function getDayRecord(date) {
  const key = keyOf(date);
  const stored = wx.getStorageSync(key);

  if (stored) {
    return stored;
  }

  const empty = createEmptyDayRecord(date);
  wx.setStorageSync(key, empty);
  return empty;
}

/**
 * 保存某一天的数据（唯一写入口）
 */
export function saveDayRecord(record) {
  record.meta.hasData = true;
  record.meta.updatedAt = Date.now();

  wx.setStorageSync(keyOf(record.date), record);
}
