// tests/BmrService.test.js
import { describe, it, expect } from 'vitest'
import { calcBMR } from '../core/services/BmrService.js'

describe('BmrService', () => {
  it('calculates male BMR correctly (Mifflin-St Jeor)', () => {
    const bmr = calcBMR({
      gender: 'male',
      age: 25,
      height: 175,
      weight: 70
    })

    expect(bmr).toBe(1674)
  })

  it('calculates female BMR correctly', () => {
    const bmr = calcBMR({
      gender: 'female',
      age: 25,
      height: 175,
      weight: 70
    })

    expect(bmr).toBe(1508)
  })
})
