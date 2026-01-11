// tests/CalcDailyBurn.test.js
import { describe, it, expect } from 'vitest'
import { calcDailyBurn } from '../core/usecases/CalcDailyBurn.js'
import { DayType } from '../core/domain/DayType.js'

describe('CalcDailyBurn (Hybrid Model)', () => {
  const profile = {
    weight: 70
  }

  const bmr = 1674

  it('uses activity factor on strength day', () => {
    const burn = calcDailyBurn({
      bmr,
      dayType: DayType.STRENGTH,
      profile
    })

    expect(burn).toBe(Math.round(1674 * 1.55))
  })

  it('uses MET-based calculation on endurance day', () => {
    const burn = calcDailyBurn({
      bmr,
      dayType: DayType.ENDURANCE,
      profile,
      workouts: [
        { met: 8, durationMin: 30 }
      ]
    })

    expect(burn).toBe(1954) // 1674 + 280
  })

  it('uses rest factor on rest day', () => {
    const burn = calcDailyBurn({
      bmr,
      dayType: DayType.REST,
      profile
    })

    expect(burn).toBe(Math.round(1674 * 1.2))
  })
})
