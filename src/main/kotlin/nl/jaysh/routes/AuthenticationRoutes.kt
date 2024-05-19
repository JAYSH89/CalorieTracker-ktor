package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.models.authentication.LoginRequest
import nl.jaysh.models.authentication.RegisterRequest
import nl.jaysh.models.authentication.toUser
import nl.jaysh.services.AuthenticationService
import org.koin.ktor.ext.inject

fun Route.authentication() {

    val authenticationService by inject<AuthenticationService>()

    route("/api") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            authenticationService.login(user = request.toUser())
        }

        post("/register") {
            val request = call.receive<RegisterRequest>()
            val response = authenticationService.register(request.toUser())
            call.respond(HttpStatusCode.Created, response)
        }
    }
}
