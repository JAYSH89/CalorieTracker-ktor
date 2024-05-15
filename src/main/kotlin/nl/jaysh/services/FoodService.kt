package nl.jaysh.services

import nl.jaysh.models.Food

class FoodService {

    private val foods = mutableListOf<Food>()

    fun getAllFood(): List<Food> {
        return foods.toList()
    }

    fun getFoodById(id: Long): Food? {
        return foods.find { food -> food.id == id }
    }

    fun createFood(food: Food): Food {
        val id = foods.maxOfOrNull { it.id ?: 1 }
        val incrementedId = id?.plus(1) ?: 1

        val newFood = food.copy(id = incrementedId)
        foods.add(newFood)

        return newFood
    }

    fun updateFood(food: Food): Food {
        val id = food.id ?: 0
        val savedFood = getFoodById(id = id) ?: throw IllegalArgumentException("Food not found $id")
        foods.remove(savedFood)

        val updatedFood = savedFood.copy(
            name = food.name,
            carbs = food.carbs,
            proteins = food.proteins,
            fats = food.fats,
            amount = food.amount,
            amountType = food.amountType,
        )
        foods.add(updatedFood)

        return updatedFood
    }

    fun deleteFood(id: Long) {
        val foodToDelete = getFoodById(id = id) ?: throw IllegalArgumentException("No food with id $id")
        foods.remove(foodToDelete)
    }
}
