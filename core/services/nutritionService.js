export function calcKcal(item, amount) {
  // amount 是“单位数量”，比如 2个 / 150ml / 100g / 1.5碗
  const kcal = item.kcalPerUnit * amount;
  // 做一个显示友好的四舍五入
  return Math.round(kcal * 10) / 10;
}
