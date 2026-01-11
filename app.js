App({
  onLaunch() {
    const hasInit = wx.getStorageSync('hasInit')

    if (hasInit) {
      // 已初始化 → 直接进日历
      wx.reLaunch({
        url: '/pages/calendar/index'
      })
    } else {
      // 未初始化 → 先去初始化页
      wx.reLaunch({
        url: '/pages/profile/index'
      })
    }
  }
})
