package nl.jaysh.data.repositories

import nl.jaysh.data.db.JournalTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.findById
import nl.jaysh.data.db.getBetween
import nl.jaysh.data.db.insert
import nl.jaysh.models.JournalEntry
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class JournalRepository {

    init {
        transaction { SchemaUtils.create(JournalTable) }
    }

    fun findById(journalEntryId: UUID, userId: UUID) = transaction {
        JournalTable.findById(journalEntryId = journalEntryId, userId = userId)
    }

    fun getBetween(startDate: LocalDateTime, endDate: LocalDateTime, userId: UUID) = transaction {
        JournalTable.getBetween(startDate = startDate, endDate = endDate, userId = userId)
    }

    fun insert(journalEntry: JournalEntry, foodId: UUID, userId: UUID) = transaction {
        JournalTable.insert(journalEntry = journalEntry, foodId = foodId, userId = userId)
    }

    fun delete(journalEntryId: UUID, userId: UUID) = transaction {
        JournalTable.delete(journalEntryId = journalEntryId, userId = userId)
    }
}
