package nl.jaysh.data.db

import nl.jaysh.models.JournalEntry
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
import java.time.LocalDateTime
import java.util.*

object JournalTable : UUIDTable(name = "journal") {
    val date: Column<LocalDateTime> = datetime(name = "date")
    val amount: Column<Double> = double(name = "amount")
    val createdAt: Column<LocalDateTime?> = datetime(name = "created_at").nullable()
    val updatedAt: Column<LocalDateTime?> = datetime(name = "updated_at").nullable()

    val food: Column<EntityID<UUID>> = reference(
        name = "food",
        refColumn = FoodTable.id,
        onDelete = ReferenceOption.CASCADE,
    )

    val user: Column<EntityID<UUID>> = reference(
        name = "user",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.CASCADE,
    )
}

fun JournalTable.getAll(userId: UUID): List<JournalEntry> = JournalTable
    .innerJoin(FoodTable)
    .selectAll()
    .where { JournalTable.user eq userId }
    .mapNotNull { row -> row.toJournalEntry() }

fun JournalTable.findById(journalEntryId: UUID, userId: UUID): JournalEntry? {
    return JournalTable
        .innerJoin(FoodTable)
        .selectAll()
        .where { (JournalTable.id eq journalEntryId) and (JournalTable.user eq userId) }
        .mapNotNull { row -> row.toJournalEntry() }
        .singleOrNull()
}

fun JournalTable.getBetween(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    userId: UUID,
): List<JournalEntry> {
    require(startDate <= endDate)

    return JournalTable
        .innerJoin(FoodTable)
        .selectAll()
        .where { (JournalTable.user eq userId) and (JournalTable.date.between(startDate, endDate)) }
        .mapNotNull { row -> row.toJournalEntry() }
}

fun JournalTable.insert(journalEntry: JournalEntry, userId: UUID): JournalEntry {
    requireNotNull(journalEntry.food.id)

    val id = insertAndGetId {
        it[date] = journalEntry.date
        it[amount] = journalEntry.amount
        it[createdAt] = LocalDateTime.now()
        it[updatedAt] = LocalDateTime.now()
        it[food] = journalEntry.food.id
        it[user] = userId
    }.value

    val newJournalEntry = findById(journalEntryId = id, userId = userId)
    requireNotNull(newJournalEntry)

    return newJournalEntry
}

fun JournalTable.delete(journalEntryId: UUID, userId: UUID) {
    val rowsChanged = deleteWhere { (JournalTable.id eq journalEntryId) and (JournalTable.user eq userId) }
    check(rowsChanged == 1)
}

fun ResultRow.toJournalEntry(): JournalEntry = JournalEntry(
    id = this[JournalTable.id].value,
    date = this[JournalTable.date],
    amount = this[JournalTable.amount],
    food = this.toFood(),
)
