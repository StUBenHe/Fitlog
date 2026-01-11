// core/services/WorkoutEnergyService.js
export function calcEnduranceEnergy({ weight, workouts }) {
  return workouts.reduce((sum, w) => {
    const hours = w.durationMin / 60
    return sum + w.met * weight * hours
  }, 0)
}
