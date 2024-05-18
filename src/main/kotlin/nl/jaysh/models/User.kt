package nl.jaysh.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.LocalDateTimeSerializer
import nl.jaysh.core.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID? = null,
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: LocalDateTime? = null,
    val gender: Gender? = null,
)

@Serializable
data class UserRequest(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val email: String,
    val password: String,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val birthday: LocalDateTime? = null,
    val gender: Gender? = null,
)

fun UserRequest.toUser(): User = User(
    id = this.id,
    email = this.email,
    password = this.password,
    firstName = this.firstName,
    lastName = this.lastName,
    birthday = this.birthday,
    gender = this.gender,
)

@Serializable
data class UserResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val email: String,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val birthday: LocalDateTime? = null,
    val gender: Gender? = null,
) {
    companion object {
        fun fromUser(user: User): UserResponse = UserResponse(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            birthday = user.birthday,
            gender = user.gender,
        )
    }
}
