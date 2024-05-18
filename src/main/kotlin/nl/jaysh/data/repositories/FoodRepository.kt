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

    fun getAll(): List<Food> = transaction {
        FoodTable.getAll()
    }

    fun add(food: Food): Food = transaction {
        FoodTable.insert(food)
    }

    fun findById(id: UUID): Food? = transaction {
        FoodTable.findById(id = id)
    }

    fun update(food: Food): Food = transaction {
        FoodTable.update(food = food)
    }

    fun delete(id: UUID) = transaction {
        FoodTable.delete(id = id)
    }
}
