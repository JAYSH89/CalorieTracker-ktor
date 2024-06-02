package nl.jaysh.services

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.data.repositories.JournalRepository
import nl.jaysh.models.journal.JournalEntry
import nl.jaysh.models.journal.JournalEntryRequest
import nl.jaysh.models.journal.Summary
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

        val journalEntry = JournalEntry(
            date = request.date,
            amount = request.amount,
            food = food,
        )

        return journalRepository.insert(journalEntry = journalEntry, userId = userId)
    }

    fun delete(journalEntryId: UUID, userId: UUID) {
        journalRepository.delete(journalEntryId = journalEntryId, userId = userId)
    }

    fun getSummaryBetween(startDate: LocalDateTime, endDate: LocalDateTime, userId: UUID): Summary {
        val journal = journalRepository.getBetween(
            startDate = startDate,
            endDate = endDate,
            userId = userId,
        )

        val totalCarbs = journal.sumOf { it.food.carbs }
        val totalProteins = journal.sumOf { it.food.proteins }
        val totalFats = journal.sumOf { it.food.fats }

        val caloriesFromCarbs = totalCarbs * 4
        val caloriesFromProteins = totalProteins * 4
        val caloriesFromFats = totalFats * 9

        return Summary(
            journal = journal,
            totalCarbs = totalCarbs,
            totalProteins = totalProteins,
            totalFats = totalFats,
            caloriesFromCarbs = caloriesFromCarbs,
            caloriesFromProteins = caloriesFromProteins,
            caloriesFromFats = caloriesFromFats,
            totalCalories = caloriesFromCarbs + caloriesFromProteins + caloriesFromFats,
        )
    }
}
