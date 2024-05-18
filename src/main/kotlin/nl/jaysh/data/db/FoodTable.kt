package nl.jaysh.data.db

import nl.jaysh.models.AmountType
import nl.jaysh.models.Food
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.util.*

object FoodTable : UUIDTable() {
    val name: Column<String> = varchar(name = "name", length = 100)
    val carbs: Column<Double> = double("carbs")
    val proteins: Column<Double> = double("proteins")
    val fats: Column<Double> = double("fats")
    val amount: Column<Double> = double("amount")
    val amountType: Column<String> = varchar(name = "amount_type", length = 50)
    val createdAt: Column<LocalDateTime?> = datetime("createdAt").nullable()
    val updatedAt: Column<LocalDateTime?> = datetime("updatedAt").nullable()
}

fun ResultRow.toFood(): Food {
    val id = this[FoodTable.id]
    val name = this[FoodTable.name]
    val carbs = this[FoodTable.carbs]
    val proteins = this[FoodTable.proteins]
    val fats = this[FoodTable.fats]
    val amount = this[FoodTable.amount]
    val amountType = this[FoodTable.amountType]

    return Food(
        id = id.value,
        name = name,
        carbs = carbs,
        proteins = proteins,
        fats = fats,
        amount = amount,
        amountType = AmountType.fromString(amountType),
    )
}

fun FoodTable.getAll(): List<Food> = selectAll().map { entity ->
    entity.toFood()
}

fun FoodTable.insert(food: Food) {
    insert {
        it[name] = food.name
        it[carbs] = food.carbs
        it[proteins] = food.proteins
        it[fats] = food.fats
        it[amount] = food.amount
        it[amountType] = food.amountType.toString()
        it[createdAt] = LocalDateTime.now()
        it[updatedAt] = LocalDateTime.now()
    }
}

fun FoodTable.update(food: Food) {
    val rowsChanged = update({ FoodTable.id eq food.id }) {
        it[name] = food.name
        it[carbs] = food.carbs
        it[proteins] = food.proteins
        it[fats] = food.fats
        it[amount] = food.amount
        it[amountType] = food.amountType.toString()
        it[updatedAt] = LocalDateTime.now()
    }
    check(rowsChanged == 1)
}

fun FoodTable.delete(id: UUID) {
    val rowsChanged = FoodTable.deleteWhere { FoodTable.id eq id }
    check(rowsChanged == 1)
}
