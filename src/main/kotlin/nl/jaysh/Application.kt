package nl.jaysh

import io.ktor.server.application.*
import io.ktor.server.cio.*
import nl.jaysh.core.config.DbConfig
import nl.jaysh.plugins.*

fun main(args: Array<String>) {
    DbConfig.setup
    EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
