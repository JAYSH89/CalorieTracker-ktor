package nl.jaysh.core.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import nl.jaysh.services.JwtService
import java.util.*

fun ApplicationCall.principalId(): UUID? = try {
    val raw = principal<JWTPrincipal>()
        ?.payload
        ?.getClaim(JwtService.ID_CLAIM)
        ?.asString()

    UUID.fromString(raw)
} catch (e: IllegalArgumentException) {
    println("error parsing JwtPrincipal: $e")
    null
}
