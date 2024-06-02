package nl.jaysh.services

import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.user.User
import java.util.UUID

class UserService(private val userRepository: UserRepository) {
    fun findById(userId: UUID): User? = userRepository.findById(userId = userId)

    fun updateUser(user: User): User = userRepository.update(user = user)

    fun deleteUser(userId: UUID) {
        userRepository.delete(userId = userId)
    }
}
