import { foodCategories } from "../../core/domain/foodCatalog";
import { calcKcal, calcProtein } from "../../core/domain/nutrition";
import { getDayRecord, saveDayRecord } from "../../core/repositories/dayRepository";

Page({
  data: {
    step: 0,

    foodCategories,
    currentCategory: null,
    currentItem: null,

    amount: 0,
    kcal: 0,
    protein: 0,

    dietEntries: [],
    totalKcal: 0,
    totalProtein: 0
  },

  onAddClick() {
    this.setData({ step: 1 });
  },

  onCategoryClick(e) {
    const category = e.currentTarget.dataset.category;
    this.setData({
      step: 2,
      currentCategory: category
    });
  },

  onItemClick(e) {
    const item = e.currentTarget.dataset.item;

    this.setData({
      step: 3,
      currentItem: item,
      amount: 0,
      kcal: 0,
      protein: 0
    });
  },

  onPlus() {
    const { currentItem, amount } = this.data;
    const step = currentItem.defaultStep || 1;
    const next = amount + step;

    this.setData({
      amount: next,
      kcal: calcKcal(currentItem, next),
      protein: calcProtein(currentItem, next)
    });
  },

  onMinus() {
    const { currentItem, amount } = this.data;
    const step = currentItem.defaultStep || 1;
    const next = amount - step;
    if (next < 0) return;

    this.setData({
      amount: next,
      kcal: calcKcal(currentItem, next),
      protein: calcProtein(currentItem, next)
    });
  },

  onConfirm() {
    const {
      currentItem,
      amount,
      kcal,
      protein,
      dietEntries
    } = this.data;

    const entry = {
      id: Date.now(),
      name: currentItem.name,
      amount,
      unit: currentItem.unit,
      kcal,
      protein
    };

    const newEntries = [...dietEntries, entry];

    const totalKcal = newEntries.reduce(
      (sum, e) => sum + e.kcal,
      0
    );

    const totalProtein = newEntries.reduce(
      (sum, e) => sum + e.protein,
      0
    );

    this.setData({
      dietEntries: newEntries,
      totalKcal,
      totalProtein,

      // 回到主界面
      step: 0,
      currentCategory: null,
      currentItem: null,
      amount: 0,
      kcal: 0,
      protein: 0
    });

    wx.showToast({
      title: "已添加",
      icon: "success"
    });
  },
  onFinishDay() {
    const { dietEntries, date } = this.data;
  
    const totalKcal = dietEntries.reduce(
      (sum, e) => sum + (Number(e.kcal) || 0),
      0
    );
  
    const totalProtein = dietEntries.reduce(
      (sum, e) => sum + (Number(e.protein) || 0),
      0

    );
  
    const day = getDayRecord(date);
  
    day.nutrition = {
      totalKcal,
      totalProtein
    };
  
    saveDayRecord(day);
  
    wx.navigateBack();

    console.log("[DIET] onFinishDay date =", date);
    console.log("[DIET] before save =", getDayRecord(date));
     saveDayRecord(day);
console.log("[DIET] after save =", getDayRecord(date));

  },




  onBack() {
    const { step } = this.data;

    if (step === 3) {
      this.setData({
        step: 2,
        currentItem: null,
        amount: 0,
        kcal: 0,
        protein: 0
      });
    } else if (step === 2) {
      this.setData({
        step: 1,
        currentCategory: null
      });
    } else if (step === 1) {
      this.setData({
        step: 0
      });
    }
  },

  onPageBack() {
    wx.navigateBack({
      delta: 1
    });
  }
});
