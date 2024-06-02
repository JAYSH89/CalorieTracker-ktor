package nl.jaysh.services

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.models.food.Food
import java.util.*

class FoodService(private val foodRepository: FoodRepository) {

    fun getAllFood(userId: UUID): List<Food> {
        return foodRepository.getAll(userId = userId)
    }

    fun findById(foodId: UUID, userId: UUID): Food? {
        return foodRepository.findById(foodId = foodId, userId = userId)
    }

    fun save(food: Food, userId: UUID): Food {
        return foodRepository.insert(food = food, userId = userId)
    }

    fun updateFood(food: Food, userId: UUID): Food {
        return foodRepository.update(food = food, userId = userId)
    }

    fun deleteFood(foodId: UUID, userId: UUID) {
        foodRepository.delete(foodId = foodId, userId = userId)
    }
}
