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
        val newUser = userRepository.createUser(user = user)
        return RegisterResponse.fromUser(user = newUser)
    }

    fun login(request: LoginRequest): LoginResponse {
        val savedUser = findByEmail(request.email) ?: throw IllegalStateException("invalid user credentials")
        if (savedUser.password != request.password) throw IllegalStateException("invalid user credentials")

        val token = jwtService.createJwtToken(request)

        return LoginResponse(
            id = savedUser.id ?: throw IllegalStateException("logged in user should have an id"),
            email = savedUser.email,
            token = token,
        )
    }

    fun getAll(): List<User> = userRepository.getAll()

    fun findById(id: UUID): User? = userRepository.findById(id = id)

    fun findByEmail(email: String): User? = userRepository.findByEmail(email = email)

    fun updateUser(user: User): User = userRepository.updateUser(user = user)

    fun deleteUser(id: UUID) {
        userRepository.deleteUser(id = id)
    }
}
