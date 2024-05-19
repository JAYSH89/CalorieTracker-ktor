package nl.jaysh.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.routes.authentication
import nl.jaysh.routes.food
import nl.jaysh.routes.user

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: $cause",
                status = HttpStatusCode.InternalServerError,
            )
        }
    }

    routing {
        authentication()
        user()
        food()
    }
}
