// core/usecases/CalcDailyBurn.js
import { DayType } from '../domain/DayType.js'
import { ACTIVITY_FACTOR } from '../domain/ActivityFactor.js'
import { calcEnduranceEnergy } from '../services/WorkoutEnergyService.js'

export function calcDailyBurn({
  bmr,
  dayType,
  profile,
  workouts = []
}) {
  // 1️⃣ 力量训练日（TDEE 模型）
  if (dayType === DayType.STRENGTH) {
    return Math.round(bmr * ACTIVITY_FACTOR.strength)
  }

  // 2️⃣ 耐力 / 有氧日（记录模型）
  if (dayType === DayType.ENDURANCE) {
    const eat = calcEnduranceEnergy({
      weight: profile.weight,
      workouts
    })
    return Math.round(bmr + eat)
  }

  // 3️⃣ 轻活动日
  if (dayType === DayType.LIGHT) {
    return Math.round(bmr * ACTIVITY_FACTOR.light)
  }

  // 4️⃣ 久坐 / 休息日
  return Math.round(bmr * ACTIVITY_FACTOR.rest)
}
