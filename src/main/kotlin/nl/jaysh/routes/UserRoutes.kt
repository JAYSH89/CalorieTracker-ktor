package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.models.UserRequest
import nl.jaysh.models.toUser
import nl.jaysh.services.UserService
import org.koin.ktor.ext.inject
import java.util.*

fun Route.user() {
    val userService by inject<UserService>()

    route("/api/user") {
        get {
            val users = userService.getAll()
            call.respond(HttpStatusCode.OK, users)
        }

        get("/{id}") {
            val id = call.parameters["id"]
                ?.toString()
                ?: throw IllegalStateException("Invalid id")

            val uuid = UUID.fromString(id)
            val user = userService.findById(id = uuid)

            if (user == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(HttpStatusCode.OK, user)
        }

        post {
            val userRequest = call.receive<UserRequest>()
            val createdUser = userService.createUser(user = userRequest.toUser())
            call.respond(HttpStatusCode.Created, createdUser)
        }

        put {
            val userRequest = call.receive<UserRequest>()
            val updatedUser = userService.updateUser(user = userRequest.toUser())
            call.respond(HttpStatusCode.OK, updatedUser)
        }

        delete("/{id}") {
            val id = call.parameters["id"]
                ?.toString()
                ?: throw IllegalStateException("Must provide id")

            val uuid = UUID.fromString(id)
            userService.deleteUser(id = uuid)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
