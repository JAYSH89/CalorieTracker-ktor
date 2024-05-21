package nl.jaysh.data.repositories

import nl.jaysh.data.db.FoodTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.findById
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.update
import nl.jaysh.models.Food
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FoodRepository {

    init {
        transaction { SchemaUtils.create(FoodTable) }
    }

    fun getAll(userId: UUID): List<Food> = transaction {
        FoodTable.getAll(userId = userId)
    }

    fun findById(foodId: UUID, userId: UUID): Food? = transaction {
        FoodTable.findById(foodId = foodId, userId = userId)
    }

    fun insert(food: Food, userId: UUID): Food = transaction {
        FoodTable.insert(food = food, userId = userId)
    }

    fun update(food: Food, userId: UUID): Food = transaction {
        FoodTable.update(food = food, userId = userId)
    }

    fun delete(foodId: UUID, userId: UUID) = transaction {
        FoodTable.delete(foodId = foodId, userId = userId)
    }
}
