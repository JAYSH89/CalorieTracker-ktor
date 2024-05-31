package nl.jaysh.models

import java.time.LocalDateTime

data class JournalEntry(
    val date: LocalDateTime,
    val amount: Double,
    val food: Food,
    val user: User,
)
