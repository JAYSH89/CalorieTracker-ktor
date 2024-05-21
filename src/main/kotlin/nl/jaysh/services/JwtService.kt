package nl.jaysh.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.auth.jwt.*
import nl.jaysh.core.utils.Constants.ACCESS_TOKEN_EXPIRATION
import nl.jaysh.core.utils.Constants.REFRESH_TOKEN_EXPIRATION
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.User
import nl.jaysh.models.authentication.JwtConfig
import java.util.*

class JwtService(
    private val userRepository: UserRepository,
    private val jwtConfig: JwtConfig,
) {

    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtConfig.secret))
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()

    fun createAccessToken(user: User): String = createJwtToken(
        id = user.id.toString(),
        email = user.email,
        expiresIn = ACCESS_TOKEN_EXPIRATION,
    )

    fun createRefreshToken(user: User): String = createJwtToken(
        id = user.id.toString(),
        email = user.email,
        expiresIn = REFRESH_TOKEN_EXPIRATION,
    )

    fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val id = extractId(credential = credential)
        val foundUser = id?.let(userRepository::findById)

        return foundUser?.let {
            if (audienceMatches(credential)) JWTPrincipal(credential.payload)
            else null
        }
    }

    fun verifyRefreshToken(token: String): DecodedJWT? {
        val decodedJWT = getDecodedJwt(token) ?: return null

        val now = System.currentTimeMillis()
        val isExpired = (decodedJWT.expiresAt?.time ?: 0L) < now
        if (isExpired) return null

        val audienceMatches = audienceMatches(decodedJWT.audience.first())
        if (!audienceMatches) return null

        val issuerMatches = issuerMatches(decodedJWT.issuer)
        if (!issuerMatches) return null

        return decodedJWT
    }

    private fun createJwtToken(id: String, email: String, expiresIn: Long): String = JWT
        .create()
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .withClaim(ID_CLAIM, id)
        .withClaim(EMAIL_CLAIM, email)
        .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
        .sign(Algorithm.HMAC256(jwtConfig.secret))

    private fun extractEmailAddress(credential: JWTCredential): String? = credential.payload
        .getClaim(EMAIL_CLAIM)
        .asString()

    private fun extractId(credential: JWTCredential): UUID? = try {
        val id = credential.payload.getClaim(ID_CLAIM).asString()
        UUID.fromString(id)
    } catch (e: IllegalArgumentException) {
        println("error: could not parse credential ${e.message}")
        null
    }

    private fun issuerMatches(issuer: String): Boolean = jwtConfig.issuer == issuer

    private fun audienceMatches(audience: String): Boolean = jwtConfig.audience == audience

    private fun audienceMatches(credential: JWTCredential): Boolean {
        return credential.payload.audience.contains(jwtConfig.audience)
    }

    private fun getDecodedJwt(token: String): DecodedJWT? = try {
        jwtVerifier.verify(token)
    } catch (e: JWTVerificationException) {
        println("error: ${e.message}")
        null
    }

    companion object {
        const val ID_CLAIM = "id"
        const val EMAIL_CLAIM = "email"
    }
}
