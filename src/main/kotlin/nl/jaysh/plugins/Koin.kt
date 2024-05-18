package nl.jaysh.plugins

import io.ktor.server.application.*
import nl.jaysh.core.di.appModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}
