// core/domain/foodCatalog.js

export const foodCategories = [
  /* ================= 蛋白质 ================= */
  {
    id: "protein",
    name: "蛋白质",
    items: [
      {
        id: "egg",
        name: "鸡蛋",
        unit: "个",
        displayNote: "1 个 ≈ 50g",
        kcalPerUnit: 70,
        proteinPerUnit: 6,
        defaultStep: 1
      },
      {
        id: "milk",
        name: "牛奶",
        unit: "杯",
        displayNote: "1 杯 ≈ 250ml",
        kcalPerUnit: 150,
        proteinPerUnit: 8,
        defaultStep: 0.5
      },
      {
        id: "yogurt",
        name: "酸奶（原味）",
        unit: "杯",
        displayNote: "1 杯 ≈ 200ml",
        kcalPerUnit: 120,
        proteinPerUnit: 7,
        defaultStep: 0.5
      },
      {
        id: "chicken_breast",
        name: "鸡胸肉",
        unit: "份",
        displayNote: "1 份 ≈ 100g",
        kcalPerUnit: 165,
        proteinPerUnit: 31,
        defaultStep: 0.5
      },
      {
        id: "beef",
        name: "牛肉（瘦）",
        unit: "份",
        displayNote: "1 份 ≈ 100g",
        kcalPerUnit: 250,
        proteinPerUnit: 26,
        defaultStep: 0.5
      },
      {
        id: "pork_lean",
        name: "瘦猪肉",
        unit: "份",
        displayNote: "1 份 ≈ 100g",
        kcalPerUnit: 240,
        proteinPerUnit: 25,
        defaultStep: 0.5
      },
      {
        id: "fish",
        name: "鱼肉",
        unit: "份",
        displayNote: "1 份 ≈ 100g",
        kcalPerUnit: 130,
        proteinPerUnit: 22,
        defaultStep: 0.5
      },
      {
        id: "shrimp",
        name: "虾",
        unit: "份",
        displayNote: "1 份 ≈ 100g",
        kcalPerUnit: 100,
        proteinPerUnit: 20,
        defaultStep: 0.5
      },
      {
        id: "tofu",
        name: "豆腐",
        unit: "块",
        displayNote: "1 块 ≈ 100g",
        kcalPerUnit: 80,
        proteinPerUnit: 8,
        defaultStep: 0.5
      }
    ]
  },

  /* ================= 蔬菜 ================= */
  {
    id: "vegetable",
    name: "蔬菜",
    items: [
      {
        id: "broccoli",
        name: "西兰花",
        unit: "份",
        displayNote: "1 份 ≈ 50g",
        kcalPerUnit: 17,
        proteinPerUnit: 1.4,
        defaultStep: 1
      },
      {
        id: "spinach",
        name: "菠菜",
        unit: "份",
        displayNote: "1 份 ≈ 50g",
        kcalPerUnit: 12,
        proteinPerUnit: 1.5,
        defaultStep: 1
      },
      {
        id: "carrot",
        name: "胡萝卜",
        unit: "份",
        displayNote: "1 份 ≈ 50g",
        kcalPerUnit: 20,
        proteinPerUnit: 0.5,
        defaultStep: 1
      },
      {
        id: "tomato",
        name: "番茄",
        unit: "个",
        displayNote: "1 个 ≈ 70g",
        kcalPerUnit: 13,
        proteinPerUnit: 0.6,
        defaultStep: 1
      }
    ]
  },

  /* ================= 水果 ================= */
  {
    id: "fruit",
    name: "水果",
    items: [
      {
        id: "apple",
        name: "苹果",
        unit: "个",
        displayNote: "1 个 ≈ 180g",
        kcalPerUnit: 95,
        proteinPerUnit: 0.5,
        defaultStep: 1
      },
      {
        id: "banana",
        name: "香蕉",
        unit: "根",
        displayNote: "1 根 ≈ 120g",
        kcalPerUnit: 105,
        proteinPerUnit: 1.3,
        defaultStep: 1
      },
      {
        id: "orange",
        name: "橙子",
        unit: "个",
        displayNote: "1 个 ≈ 150g",
        kcalPerUnit: 62,
        proteinPerUnit: 1.2,
        defaultStep: 1
      }
    ]
  },

  /* ================= 主食 ================= */
  {
    id: "staple",
    name: "主食",
    items: [
      {
        id: "rice",
        name: "米饭",
        unit: "碗",
        displayNote: "1 碗 ≈ 200g 熟饭",
        kcalPerUnit: 200,
        proteinPerUnit: 4,
        defaultStep: 0.5
      },
      {
        id: "noodle_wheat",
        name: "普通面条",
        unit: "g",
        displayNote: "25g 干面 ≈ 1 小把",
        kcalPerUnit: 87,
        proteinPerUnit: 3,
        defaultStep: 25
      },
      {
        id: "noodle_pasta",
        name: "意面",
        unit: "g",
        displayNote: "25g 干意面 ≈ 1 小把",
        kcalPerUnit: 90,
        proteinPerUnit: 3.5,
        defaultStep: 25
      },
      {
        id: "noodle_instant",
        name: "泡面",
        unit: "g",
        displayNote: "25g 泡面 ≈ 1/4 包",
        kcalPerUnit: 120,
        proteinPerUnit: 2.5,
        defaultStep: 25
      },
      {
        id: "bread",
        name: "面包",
        unit: "片",
        displayNote: "1 片 ≈ 30g",
        kcalPerUnit: 80,
        proteinPerUnit: 3,
        defaultStep: 1
      },
      {
        id: "oatmeal",
        name: "燕麦",
        unit: "份",
        displayNote: "1 份 ≈ 40g 干燕麦",
        kcalPerUnit: 150,
        proteinPerUnit: 6,
        defaultStep: 0.5
      }
    ]
  }
];
