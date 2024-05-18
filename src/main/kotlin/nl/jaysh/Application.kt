package nl.jaysh

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import nl.jaysh.core.config.DbConfig
import nl.jaysh.plugins.*

fun main() {
    DbConfig.setup
    embeddedServer(
        CIO,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
