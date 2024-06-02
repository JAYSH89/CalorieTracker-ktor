package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.core.utils.principalId
import nl.jaysh.models.user.UserRequest
import nl.jaysh.models.user.UserResponse
import nl.jaysh.models.user.toUser
import nl.jaysh.services.UserService
import org.koin.ktor.ext.inject

fun Route.user() {
    val userService by inject<UserService>()

    route("/api/user") {
        authenticate {
            get("/me") {
                call.principalId()?.let { userId ->
                    val user = userService.findById(userId = userId)
                    if (user == null)
                        call.respond(HttpStatusCode.NotFound)
                    else
                        call.respond(HttpStatusCode.OK, UserResponse.fromUser(user))

                } ?: call.respond(HttpStatusCode.BadRequest)
            }
        }

        authenticate {
            put {
                call.principalId()?.let { id ->
                    val userRequest = call.receive<UserRequest>()

                    if (userRequest.id != id) {
                        call.respond(HttpStatusCode.BadRequest)
                    } else {
                        val updatedUser = userService.updateUser(user = userRequest.toUser())
                        call.respond(HttpStatusCode.OK, UserResponse.fromUser(updatedUser))
                    }
                } ?: call.respond(HttpStatusCode.BadRequest)
            }
        }

        authenticate {
            delete {
                call.principalId()?.let { userId ->
                    val user = userService.findById(userId = userId)

                    if (user?.id != userId) {
                        call.respond(HttpStatusCode.BadRequest)
                    } else {
                        userService.deleteUser(userId = userId)
                        call.respond(HttpStatusCode.NoContent)
                    }
                } ?: call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
