package nl.jaysh.services

import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.User
import nl.jaysh.models.authentication.LoginRequest
import nl.jaysh.models.authentication.LoginResponse
import nl.jaysh.models.authentication.RegisterRequest
import nl.jaysh.models.authentication.RegisterResponse
import nl.jaysh.models.authentication.toUser
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
) {

    fun register(request: RegisterRequest): RegisterResponse {
        val user = request.toUser()
        val newUser = userRepository.insert(user = user)
        return RegisterResponse.fromUser(user = newUser)
    }

    fun login(request: LoginRequest): LoginResponse {
        val savedUser = userRepository
            .findByEmail(email = request.email)
            ?: throw IllegalStateException("invalid user credentials")

        if (savedUser.password != request.password) throw IllegalStateException("invalid user credentials")

        val token = jwtService.createJwtToken(request)

        return LoginResponse(
            id = savedUser.id ?: throw IllegalStateException("logged in user should have an id"),
            email = savedUser.email,
            token = token,
        )
    }

    fun findById(userId: UUID): User? = userRepository.findById(userId = userId)

    fun updateUser(user: User): User = userRepository.update(user = user)

    fun deleteUser(userId: UUID) {
        userRepository.delete(userId = userId)
    }
}
