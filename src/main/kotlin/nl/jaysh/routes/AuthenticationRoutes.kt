package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.models.authentication.AuthRequest
import nl.jaysh.models.authentication.RefreshTokenRequest
import nl.jaysh.services.AuthService
import org.koin.ktor.ext.inject

fun Route.authentication() {
    val authService by inject<AuthService>()

    route("/api/auth") {

        post("/register") {
            val request = call.receive<AuthRequest>()
            val response = authService.register(request = request)
            call.respond(HttpStatusCode.Created, response)
        }

        post("/login") {
            val request = call.receive<AuthRequest>()
            val response = authService.login(request = request)

            response?.let { token ->
                call.respond(HttpStatusCode.OK, token)
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }

        post("/refresh") {
            val request = call.receive<RefreshTokenRequest>()
            val accessToken = authService.refresh(refreshToken = request.refreshToken)

            accessToken?.let { token ->
                val response = mapOf("access_token" to token)
                call.respond(HttpStatusCode.OK, response)
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }
    }
}
