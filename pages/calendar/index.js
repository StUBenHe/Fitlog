import { getDayRecord } from "../../core/repositories/dayRepository";

Page({
  data: {
    currentIndex: 0,
    days: []
  },

  onLoad() {
    const days = this.generateDays(7)
    this.setData({
      days,
      currentIndex: 7
    })
  },

  onShow() {
    const days = this.generateDays(7)
    this.setData({ days })
  },

  generateDays(n) {
    const today = new Date()
    const days = []

    for (let i = -n; i <= n; i++) {
      const d = new Date(today)
      d.setDate(today.getDate() + i)

      const date = d.toISOString().slice(0, 10)
      const dayRecord = getDayRecord(date)

      const hasNutrition = !!dayRecord.nutrition

      days.push({
        date,
        weekday: '周' + '日一二三四五六'[d.getDay()],

        // 🍽 饮食（真实数据源）
        diet: {
          status: hasNutrition ? 'done' : 'empty',
          calories: hasNutrition ? dayRecord.nutrition.totalKcal : 0
        },

        // 🏋️ 训练（预留）
        workout: {
          type: dayRecord.training?.type || 'REST',
          calories: dayRecord.training?.calories || 0
        }
      })
    }

    return days
  },

  goDiet(e) {
    const date = e.currentTarget.dataset.date
    wx.navigateTo({
      url: `/pages/diet/index?date=${date}`
    })
  },

  goWorkout(e) {
    const date = e.currentTarget.dataset.date
    wx.navigateTo({
      url: `/pages/workout/index?date=${date}`
    })
  }
})
