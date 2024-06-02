package nl.jaysh.data.db

import nl.jaysh.models.Weight
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

object WeightTable : UUIDTable() {
    val weight: Column<Double> = double(name = "carbs")
    val measuredAt: Column<LocalDateTime> = datetime(name = "measured_at")
    val createdAt: Column<LocalDateTime?> = datetime(name = "created_at").nullable()
    val updatedAt: Column<LocalDateTime?> = datetime(name = "updated_at").nullable()

    val user: Column<EntityID<UUID>> = reference(
        name = "user",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.CASCADE,
    )
}

fun WeightTable.getAll(userId: UUID): List<Weight> = selectAll()
    .where { user eq userId }
    .map(ResultRow::toWeight)

fun WeightTable.findById(weightId: UUID, userId: UUID): Weight? = selectAll()
    .where { (id eq weightId) and (user eq userId) }
    .map(ResultRow::toWeight)
    .singleOrNull()

fun WeightTable.insert(weightEntry: Weight, userId: UUID): Weight {
    val id = insertAndGetId {
        it[weight] = weightEntry.weight
        it[measuredAt] = LocalDateTime.of(weightEntry.measuredAt, LocalTime.MIN)
        it[createdAt] = LocalDateTime.now()
        it[updatedAt] = LocalDateTime.now()
        it[user] = userId
    }.value

    val newWeightEntry = findById(weightId = id, userId = userId)
    requireNotNull(newWeightEntry)

    return newWeightEntry
}

fun WeightTable.update(weightEntry: Weight, userId: UUID): Weight {
    requireNotNull(weightEntry.id)

    val rowsChanged = update({ (id eq weightEntry.id) and (user eq userId) }) {
        it[weight] = weightEntry.weight
        it[measuredAt] = LocalDateTime.of(weightEntry.measuredAt, LocalTime.MIN)
        it[updatedAt] = LocalDateTime.now()
    }
    check(rowsChanged == 1)

    val updatedWeightEntry = findById(weightId = weightEntry.id, userId = userId)
    requireNotNull(updatedWeightEntry)

    return updatedWeightEntry
}

fun WeightTable.delete(weightId: UUID, userId: UUID) {
    val rowsChanged = deleteWhere { (id eq weightId) and (user eq userId) }
    check(rowsChanged == 1)
}

fun ResultRow.toWeight(): Weight = Weight(
    id = this[WeightTable.id].value,
    weight = this[WeightTable.weight],
    measuredAt = this[WeightTable.measuredAt].toLocalDate(),
)
