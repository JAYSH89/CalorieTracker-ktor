package nl.jaysh.services

import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.User
import nl.jaysh.models.authentication.RegisterResponse

class AuthenticationService(private val userRepository: UserRepository) {

    fun register(user: User): RegisterResponse {
        val newUser = userRepository.createUser(user = user)
        return RegisterResponse.fromUser(user = newUser)
    }

    fun login(user: User) {

    }
}
