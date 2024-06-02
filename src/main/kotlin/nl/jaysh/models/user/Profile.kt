package nl.jaysh.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.LocalDateTimeSerializer
import nl.jaysh.models.Gender
import java.time.LocalDateTime

@Serializable
data class Profile(
    @SerialName("first_name")
    val firstName: String? = null,

    @SerialName("last_name")
    val lastName: String? = null,

    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("date_of_birth")
    val dateOfBirth: LocalDateTime,

    @SerialName("height")
    val height: Double,

    @SerialName("gender")
    val gender: Gender,

    @SerialName("activity_factor")
    val activityFactor: ActivityFactor,
)
