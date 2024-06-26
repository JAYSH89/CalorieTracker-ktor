package nl.jaysh.helpers.objects

import nl.jaysh.models.Food
import nl.jaysh.models.JournalEntry
import java.time.LocalDateTime

object TestJournalEntry {
    fun default(food: Food): JournalEntry = JournalEntry(
        date = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0),
        amount = 1.0,
        food = food,
    )
}
