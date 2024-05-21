package nl.jaysh.services

import nl.jaysh.core.utils.Constants
import nl.jaysh.core.utils.Constants.ACCESS_TOKEN_EXPIRATION
import nl.jaysh.data.repositories.TokenRepository
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.authentication.AuthRequest
import nl.jaysh.models.authentication.AuthResponse
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val jwtService: JwtService,
) {
    fun register(request: AuthRequest): AuthResponse {
        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt())

        val user = userRepository.insert(email = request.email, password = hashedPassword)
        val accessToken = jwtService.createAccessToken(user = user)
        val refreshToken = jwtService.createRefreshToken(user = user)

        tokenRepository.save(refreshToken, user)

        return AuthResponse(
            tokenType = "Bearer",
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = ACCESS_TOKEN_EXPIRATION,
        )
    }

    // TODO encryption + proper error handling
    fun login(request: AuthRequest): AuthResponse? {
        val user = userRepository.findByEmail(email = request.email) ?: return null
        if (!BCrypt.checkpw(request.password, user.password)) return null

        val accessToken = jwtService.createAccessToken(user = user)
        val refreshToken = jwtService.createRefreshToken(user = user)

        tokenRepository.save(refreshToken, user)

        return AuthResponse(
            tokenType = "Bearer",
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = Constants.ACCESS_TOKEN_EXPIRATION,
        )
    }

    fun refresh(refreshToken: String): String? {
        val decodedRefreshToken = jwtService.verifyRefreshToken(refreshToken)
        val user = tokenRepository.findUserByToken(refreshToken)

        if (decodedRefreshToken != null && user != null) {
            val id = decodedRefreshToken.getClaim(JwtService.ID_CLAIM).asString()

            if (id == user.id.toString())
                return jwtService.createAccessToken(user)

            return null
        }

        return null
    }
}
