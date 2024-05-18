package nl.jaysh.models

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID? = null,
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: LocalDateTime? = null,
    val gender: String? = null,
)
