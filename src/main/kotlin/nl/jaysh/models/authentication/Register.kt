package nl.jaysh.models.authentication

import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.UUIDSerializer
import nl.jaysh.models.User
import java.util.UUID

@Serializable
data class RegisterRequest(val email: String, val password: String)

fun RegisterRequest.toUser(): User = User(
    email = email,
    password = password,
)

@Serializable
data class RegisterResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val email: String,
) {
    companion object {
        fun fromUser(user: User): RegisterResponse = RegisterResponse(
            id = user.id!!,
            email = user.email,
        )
    }
}
