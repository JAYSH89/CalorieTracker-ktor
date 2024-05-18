package nl.jaysh.services

import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.User
import java.util.UUID

class UserService(private val userRepository: UserRepository) {

    fun getAll(): List<User> = userRepository.getAll()

    fun findById(id: UUID): User? = userRepository.findById(id = id)

    fun createUser(user: User): User = userRepository.createUser(user = user)

    fun updateUser(user: User): User = userRepository.updateUser(user = user)

    fun deleteUser(id: UUID) {
        userRepository.deleteUser(id = id)
    }
}
