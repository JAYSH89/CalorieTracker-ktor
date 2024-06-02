package nl.jaysh.models

import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.LocalDateTimeSerializer
import nl.jaysh.core.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Weight(
    @Serializable(with = UUIDSerializer::class) val id: UUID?,
    val weight: Double,
    @Serializable(with = LocalDateTimeSerializer::class) val measuredAt: LocalDateTime,
)
