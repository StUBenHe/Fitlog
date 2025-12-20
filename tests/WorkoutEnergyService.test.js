// tests/WorkoutEnergyService.test.js
import { describe, it, expect } from 'vitest'
import { calcEnduranceEnergy } from '../core/services/WorkoutEnergyService.js'

describe('WorkoutEnergyService (Endurance)', () => {
  it('calculates endurance workout energy using MET', () => {
    const energy = calcEnduranceEnergy({
      weight: 70,
      workouts: [
        { met: 8, durationMin: 30 }
      ]
    })

    expect(energy).toBe(280)
  })
})
