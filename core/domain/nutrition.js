// core/domain/nutrition.js

export function calcKcal(item, amount) {
  if (!item || typeof item.kcalPerUnit !== "number") {
    return 0;
  }
  return Math.round(item.kcalPerUnit * amount * 10) / 10;
}

export function calcProtein(item, amount) {
  if (!item || typeof item.proteinPerUnit !== "number") {
    return 0;
  }
  return Math.round(item.proteinPerUnit * amount * 10) / 10;
}
