package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.models.authentication.LoginRequest
import nl.jaysh.models.authentication.RegisterRequest
import nl.jaysh.services.UserService
import org.koin.ktor.ext.inject

fun Route.authentication() {

    val service by inject<UserService>()

    route("/api") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val response = service.login(request = request)

            response.token
                ?.let { call.respond(response) }
                ?: call.respond(HttpStatusCode.Unauthorized)
        }

        post("/register") {
            val request = call.receive<RegisterRequest>()
            val response = service.register(request = request)
            call.respond(HttpStatusCode.Created, response)
        }
    }
}
