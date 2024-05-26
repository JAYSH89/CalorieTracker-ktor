package nl.jaysh.models

import java.time.LocalDateTime

data class RefreshToken(
    val token: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val user: User,
)
