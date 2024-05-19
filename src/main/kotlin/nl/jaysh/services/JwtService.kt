package nl.jaysh.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.*
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.authentication.JwtConfig
import java.util.*

class JwtService(
    private val repository: UserRepository,
    private val jwtConfig: JwtConfig,
) {
    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.secret))
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()

    fun createJwtToken(email: String): String? = JWT.create()
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .withClaim(CLAIM_NAME, email)
        .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
        .sign(Algorithm.HMAC256(jwtConfig.secret))

    fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val email = extractEmailAddress(credential = credential)
        val foundUser = email?.let(repository::findByEmail)

        return foundUser?.let {
            if (audienceMatches(credential)) JWTPrincipal(credential.payload)
            else null
        }
    }

    private fun audienceMatches(credential: JWTCredential): Boolean = credential.payload.audience
        .contains(jwtConfig.audience)

    private fun extractEmailAddress(credential: JWTCredential): String? = credential.payload
        .getClaim(CLAIM_NAME)
        .asString()

    companion object {
        const val CLAIM_NAME = "email"
    }
}
