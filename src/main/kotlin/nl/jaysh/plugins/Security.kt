package nl.jaysh.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import nl.jaysh.models.authentication.JwtConfig
import nl.jaysh.services.JwtService
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val jwtConfig: JwtConfig by inject<JwtConfig>()
    val jwtService by inject<JwtService>()

    install(Authentication) {
        jwt {
            realm = jwtConfig.realm
            verifier(jwtService.jwtVerifier)
            validate { credential -> jwtService.customValidator(credential = credential) }
        }
    }
}
