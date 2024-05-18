package nl.jaysh.data.repositories

import nl.jaysh.data.db.FoodTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.toFood
import nl.jaysh.data.db.update
import nl.jaysh.models.Food
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class FoodRepository {
    fun getAll(): List<Food> = FoodTable.getAll()

    fun add(food: Food) {
        FoodTable.insert(food)
    }

    fun findById(id: UUID): Food? = FoodTable
        .selectAll()
        .where { FoodTable.id eq id }
        .map { entity -> entity.toFood() }
        .singleOrNull()

    fun update(food: Food) {
        FoodTable.update(food = food)
    }

    fun delete(id: UUID) {
        FoodTable.delete(id = id)
    }
}



