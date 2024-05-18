package nl.jaysh.services

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.models.Food
import java.util.*

class FoodService(private val foodRepository: FoodRepository) {

    fun getAllFood(): List<Food> = foodRepository.getAll()

    fun getFoodById(id: UUID): Food? = foodRepository.findById(id = id)

    fun createFood(food: Food): Food = foodRepository.add(food = food)

    fun updateFood(food: Food): Food = foodRepository.update(food = food)

    fun deleteFood(id: UUID) {
        foodRepository.delete(id = id)
    }
}
