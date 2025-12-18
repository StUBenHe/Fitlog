import { calcBMR } from '../core/services/BmrService.js'

console.log(
  calcBMR({
    gender: 'male',
    age: 25,
    height: 175,
    weight: 70
  })
)
