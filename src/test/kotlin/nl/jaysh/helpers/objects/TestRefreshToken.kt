package nl.jaysh.helpers.objects

import nl.jaysh.models.authentication.RefreshToken
import java.time.LocalDateTime

val testRefreshToken = RefreshToken(
    token = "exampleToken",
    issuedAt = LocalDateTime.of(2024, 6, 1, 0, 0, 0, 0),
    expiresAt = LocalDateTime.of(2024, 7, 1, 0, 0, 0, 0),
    user = testUser,
)
