package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.models.Food
import nl.jaysh.services.FoodService
import org.koin.ktor.ext.inject
import java.util.*

fun Route.food() {

    val foodService by inject<FoodService>()

    route("/api/food") {
        authenticate {
            get {
                val foods = foodService.getAllFood();
                call.respond(HttpStatusCode.OK, foods)
            }
        }

        authenticate {
            get("/{id}") {
                val id = call.parameters["id"]
                    ?.toString()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                val uuid = UUID.fromString(id)
                val food = foodService.findById(id = uuid)

                if (food == null)
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(HttpStatusCode.OK, food)
            }
        }

        authenticate {
            post {
                val food = call.receive<Food>()
                val createdFood = foodService.createFood(food = food)
                call.respond(HttpStatusCode.Created, createdFood)
            }
        }

        authenticate {
            put {
                val food = call.receive<Food>()
                val updatedFood = foodService.updateFood(food = food)
                call.respond(HttpStatusCode.OK, updatedFood)
            }
        }

        authenticate {
            delete("/{id}") {
                val id = call.parameters["id"]
                    ?.toString()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)

                val uuid = UUID.fromString(id)
                foodService.deleteFood(id = uuid)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
