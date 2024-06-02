package nl.jaysh.helpers.objects

import nl.jaysh.models.food.AmountType
import nl.jaysh.models.food.Food

val testFood = Food(
    name = "Egg",
    carbs = 1.0,
    proteins = 1.0,
    fats = 1.0,
    amount = 1.0,
    amountType = AmountType.UNIT,
)
