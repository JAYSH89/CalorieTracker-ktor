package nl.jaysh.data.repositories

import nl.jaysh.data.db.JournalTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.findById
import nl.jaysh.data.db.getAll
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

    fun getAll(userId: UUID): List<JournalEntry> = transaction {
        JournalTable.getAll(userId = userId)
    }

    fun findById(journalEntryId: UUID, userId: UUID): JournalEntry? = transaction {
        JournalTable.findById(journalEntryId = journalEntryId, userId = userId)
    }

    fun getBetween(startDate: LocalDateTime, endDate: LocalDateTime, userId: UUID): List<JournalEntry> = transaction {
        JournalTable.getBetween(
            startDate = startDate,
            endDate = endDate,
            userId = userId,
        )
    }

    fun insert(journalEntry: JournalEntry, userId: UUID): JournalEntry = transaction {
        JournalTable.insert(journalEntry = journalEntry, userId = userId)
    }

    fun delete(journalEntryId: UUID, userId: UUID) = transaction {
        JournalTable.delete(journalEntryId = journalEntryId, userId = userId)
    }
}
