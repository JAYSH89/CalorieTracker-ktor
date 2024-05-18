package nl.jaysh.services

import nl.jaysh.models.Food
import java.util.*

class FoodService {

    private val foods = mutableListOf<Food>()

    fun getAllFood(): List<Food> = foods.toList()

    fun getFoodById(id: UUID): Food? = foods.find { food ->
        food.id == id
    }

    fun createFood(food: Food): Food {
        val newFood = food.copy(id = UUID.randomUUID())
        foods.add(newFood)
        return newFood
    }

    fun updateFood(food: Food): Food {
        val id = food.id ?: throw IllegalArgumentException("invalid id")
        val savedFood = getFoodById(id = id) ?: throw IllegalArgumentException("food not found $id")
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

    fun deleteFood(id: UUID) {
        val foodToDelete = getFoodById(id = id) ?: throw IllegalArgumentException("No food with id $id")
        foods.remove(foodToDelete)
    }
}
