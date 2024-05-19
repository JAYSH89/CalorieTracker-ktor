package nl.jaysh.plugins

import io.ktor.server.application.*
import nl.jaysh.core.di.appModule
import nl.jaysh.models.authentication.JwtConfig
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    val jwtConfig = JwtConfig(
        secret = getConfigProperty(path = "jwt.secret"),
        issuer = getConfigProperty(path = "jwt.issuer"),
        audience = getConfigProperty(path = "jwt.audience"),
        realm = getConfigProperty(path = "jwt.realm"),
    )

    install(Koin) {
        slf4jLogger()
        modules(appModule(jwtConfig))
    }
}

private fun Application.getConfigProperty(path: String): String = environment.config
    .property(path)
    .getString()
