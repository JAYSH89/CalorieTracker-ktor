package nl.jaysh.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.UUIDSerializer
import java.util.*

@Serializable
data class UserRequest(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,

    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String,
)

fun UserRequest.toUser(): User = User(
    id = this.id,
    email = this.email,
    password = this.password,
)
