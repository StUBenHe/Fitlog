// adapters/wechat/mapProfileForm.js
function mapProfileForm(form) {
  return {
    gender: form.gender,
    age: Number(form.age),
    height: Number(form.height),
    weight: Number(form.weight),
    bodyFat: form.bodyFat ? Number(form.bodyFat) : null
  }
}

module.exports = { mapProfileForm }
