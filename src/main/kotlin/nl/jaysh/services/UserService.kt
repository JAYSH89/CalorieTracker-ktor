package nl.jaysh.services

import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.User
import nl.jaysh.models.UserResponse
import java.util.UUID

class UserService(private val userRepository: UserRepository) {

    fun getAll(): List<UserResponse> = userRepository
        .getAll()
        .map(UserResponse::fromUser)

    fun findById(id: UUID): UserResponse? {
        val user = userRepository.findById(id = id)
        return user?.let(UserResponse::fromUser)
    }

    fun createUser(user: User): UserResponse {
        val newUser = userRepository.createUser(user = user)
        return UserResponse.fromUser(user = newUser)
    }

    fun updateUser(user: User): UserResponse {
        val updatedUser = userRepository.updateUser(user = user)
        return UserResponse.fromUser(user = updatedUser)
    }

    fun deleteUser(id: UUID) {
        userRepository.deleteUser(id = id)
    }
}
