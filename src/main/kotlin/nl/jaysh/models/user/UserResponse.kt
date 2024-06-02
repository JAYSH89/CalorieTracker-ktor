package nl.jaysh.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.UUIDSerializer
import java.util.*

@Serializable
data class UserResponse(
    @Serializable(with = UUIDSerializer::class)
    @SerialName("id")
    val id: UUID,

    @SerialName("email")
    val email: String,

    @SerialName("profile")
    val profile: Profile? = null,
) {
    companion object {
        fun fromUser(user: User): UserResponse = UserResponse(
            id = user.id,
            email = user.email,
        )
    }
}
