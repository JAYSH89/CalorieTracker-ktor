package nl.jaysh.services

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.data.repositories.JournalRepository
import nl.jaysh.models.JournalEntry
import nl.jaysh.models.JournalEntryRequest
import java.time.LocalDateTime
import java.util.*

class JournalService(
    private val journalRepository: JournalRepository,
    private val foodRepository: FoodRepository,
) {

    fun getAllJournalEntries(userId: UUID): List<JournalEntry> {
        return journalRepository.getAll(userId = userId)
    }

    fun findById(journalEntryId: UUID, userId: UUID): JournalEntry? {
        return journalRepository.findById(journalEntryId = journalEntryId, userId = userId)
    }

    fun getBetween(startDate: LocalDateTime, endDate: LocalDateTime, userId: UUID): List<JournalEntry> {
        return journalRepository.getBetween(startDate = startDate, endDate = endDate, userId = userId)
    }

    fun save(request: JournalEntryRequest, userId: UUID): JournalEntry {
        val food = foodRepository.findById(foodId = request.food, userId = userId)
        requireNotNull(food)

        println("FOUND FOOD: $food")

        val journalEntry = JournalEntry(
            date = request.date,
            amount = request.amount,
            food = food,
        )

        println("CREATED ENTRY: $journalEntry")

        return journalRepository.insert(journalEntry = journalEntry, userId = userId)
    }

    fun delete(journalEntryId: UUID, userId: UUID) {
        journalRepository.delete(journalEntryId = journalEntryId, userId = userId)
    }
}
