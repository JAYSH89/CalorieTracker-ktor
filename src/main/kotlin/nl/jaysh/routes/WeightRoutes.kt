package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.core.utils.principalId
import nl.jaysh.models.weight.Weight
import nl.jaysh.services.WeightService
import org.koin.ktor.ext.inject
import java.util.*

fun Route.weight() {
    val weightService by inject<WeightService>()

    route("/api/weight") {
        authenticate {
            get {
                call.principalId()?.let { userId ->
                    val weights = weightService.getAll(userId = userId)
                    call.respond(HttpStatusCode.OK, weights)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            post {
                val createWeight = call.receive<Weight>()

                call.principalId()?.let { userId ->
                    val createdWeight = weightService.insert(weight = createWeight, userId = userId)
                    call.respond(HttpStatusCode.Created, createdWeight)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            put {
                val createWeight = call.receive<Weight>()

                call.principalId()?.let { userId ->
                    val updatedWeight = weightService.update(weight = createWeight, userId = userId)
                    call.respond(HttpStatusCode.OK, updatedWeight)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            delete("/{id}") {
                val requestParam = call.parameters["id"]
                    ?.toString()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)

                val weightId = UUID.fromString(requestParam)

                call.principalId()?.let { userId ->
                    weightService.delete(weightId = weightId, userId = userId)
                    call.respond(HttpStatusCode.NoContent)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
