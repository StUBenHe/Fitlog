// core/domain/NutritionSummary.js
export function createNutritionSummary(totalKcal, totalProtein) {
  return {
    totalKcal,
    totalProtein,
  };
}
