package nl.jaysh.models

import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.LocalDateTimeSerializer
import nl.jaysh.core.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*

@Serializable
data class JournalEntry(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime,
    val amount: Double,
    val food: Food,
)

@Serializable
data class JournalEntryRequest(
    @Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime,
    val amount: Double,
    @Serializable(with = UUIDSerializer::class) val food: UUID,
)

@Serializable
data class JournalEntryResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime,
    val amount: Double,
    val food: Food,
)
