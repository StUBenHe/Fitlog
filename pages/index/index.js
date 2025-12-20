Page({
  data: {
    form: {
      gender: 'male',
      age: '',
      height: '',
      weight: '',
      bodyFat: ''
    }
  },

  onInput(e) {
    const key = e.currentTarget.dataset.key
    this.setData({
      [`form.${key}`]: e.detail.value
    })
  },

  onGenderChange(e) {
    this.setData({
      'form.gender': e.detail.value
    })
  },

  onSubmit() {
    const { form } = this.data
  
    if (!form.age || !form.height || !form.weight) {
      wx.showToast({ title: '请填写完整信息', icon: 'none' })
      return
    }
  
    wx.setStorageSync('userProfile', form)
    wx.setStorageSync('hasInit', true)
  
    wx.redirectTo({
      url: '/pages/calendar/index'
    })
  }
  
})
