export function calcBMR(profile) {
  if (profile.bodyFat != null) {
    const lean = profile.weight * (1 - profile.bodyFat)
    return Math.round(370 + 21.6 * lean)
  }
// BMR 计算 男女
  if (profile.gender === 'male') {
    return Math.round(
      10 * profile.weight +
      6.25 * profile.height -
      5 * profile.age + 5
    )
  }
  else
  return Math.round(
    10 * profile.weight +
    6.25 * profile.height -
    5 * profile.age - 161
  )
}
