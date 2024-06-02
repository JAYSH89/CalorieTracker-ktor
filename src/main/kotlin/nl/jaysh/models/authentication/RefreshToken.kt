package nl.jaysh.models.authentication

import nl.jaysh.models.user.User
import java.time.LocalDateTime

data class RefreshToken(
    val token: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val user: User,
)
