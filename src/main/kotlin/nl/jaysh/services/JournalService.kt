package nl.jaysh.services

import nl.jaysh.data.repositories.JournalRepository
import nl.jaysh.models.JournalEntry
import java.time.LocalDateTime
import java.util.*

class JournalService(private val journalRepository: JournalRepository) {

    fun findById(journalEntryId: UUID, userId: UUID): JournalEntry? {
        return journalRepository.findById(journalEntryId = journalEntryId, userId = userId)
    }

    fun getBetween(startDate: LocalDateTime, endDate: LocalDateTime, userId: UUID): List<JournalEntry> {
        return journalRepository.getBetween(startDate = startDate, endDate = endDate, userId = userId)
    }

    fun insert(journalEntry: JournalEntry, foodId: UUID, userId: UUID): JournalEntry {
        return journalRepository.insert(journalEntry = journalEntry, userId = userId)
    }

    fun delete(journalEntryId: UUID, userId: UUID) {
        journalRepository.delete(journalEntryId = journalEntryId, userId = userId)
    }
}
