package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.core.utils.principalEmail
import nl.jaysh.models.UserRequest
import nl.jaysh.models.toUser
import nl.jaysh.services.UserService
import org.koin.ktor.ext.inject
import java.util.*

fun Route.user() {
    val userService by inject<UserService>()

    route("/api/user") {
        authenticate {
            get("/{id}") {
                val id = call.parameters["id"]
                    ?.toString()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                val uuid = UUID.fromString(id)
                val user = userService.findById(id = uuid)

                when {
                    user == null -> call.respond(HttpStatusCode.NotFound)
                    user.email != call.principalEmail() -> call.respond(HttpStatusCode.NotFound)
                    else -> call.respond(HttpStatusCode.OK, user)
                }
            }
        }

        authenticate {
            put {
                val userRequest = call.receive<UserRequest>()
                if (userRequest.email != call.principalEmail()) {
                    return@put call.respond(HttpStatusCode.NotFound)
                }

                val updatedUser = userService.updateUser(user = userRequest.toUser())
                call.respond(HttpStatusCode.OK, updatedUser)
            }
        }

        authenticate {
            delete("/{id}") {
                val id = call.parameters["id"]
                    ?.toString()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)

                val uuid = UUID.fromString(id)
                val user = userService.findById(id = uuid)

                if (user?.email != call.principalEmail()) {
                    return@delete call.respond(HttpStatusCode.NotFound)
                }

                userService.deleteUser(id = uuid)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
