package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.food() {
    route("/food") {
        get {
            call.respond("GET ALL FOOD")
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id")
            call.respond("GET FOOD BY ID $id")
        }

        post {
            call.respond(HttpStatusCode.Created, "CREATED FOOD")
        }

        put {
            call.respond(HttpStatusCode.OK, "UPDATED FOOD")
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id")
            call.respond(HttpStatusCode.NoContent, "DELETED FOOD")
        }
    }
}
