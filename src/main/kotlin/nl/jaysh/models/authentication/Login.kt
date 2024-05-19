package nl.jaysh.models.authentication

import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.UUIDSerializer
import nl.jaysh.models.User
import java.util.UUID

@Serializable
data class LoginRequest(val email: String, val password: String, )

fun LoginRequest.toUser(): User = User(
    email = email,
    password = password,
)

@Serializable
data class LoginResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val email: String,
)
