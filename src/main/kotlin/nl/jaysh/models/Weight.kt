package nl.jaysh.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.LocalDateSerializer
import nl.jaysh.core.utils.UUIDSerializer
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Weight(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,

    val weight: Double,

    @Serializable(with = LocalDateSerializer::class)
    @SerialName("measured_at")
    val measuredAt: LocalDate,
)
