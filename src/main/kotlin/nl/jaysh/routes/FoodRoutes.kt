package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.core.utils.principalId
import nl.jaysh.models.food.Food
import nl.jaysh.services.FoodService
import org.koin.ktor.ext.inject
import java.util.*

fun Route.food() {
    val foodService by inject<FoodService>()

    route("/api/food") {
        authenticate {
            get {
                call.principalId()?.let { userId ->
                    val foods = foodService.getAllFood(userId = userId);
                    call.respond(HttpStatusCode.OK, foods)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            get("/{id}") {
                val requestParam = call.parameters["id"]
                    ?.toString()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                val foodId = UUID.fromString(requestParam)
                call.principalId()?.let { userId ->
                    val food = foodService.findById(foodId = foodId, userId = userId)

                    if (food == null)
                        call.respond(HttpStatusCode.NotFound)
                    else
                        call.respond(HttpStatusCode.OK, food)

                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            post {
                val createFood = call.receive<Food>()

                call.principalId()?.let { userId ->
                    val createdFood = foodService.save(food = createFood, userId = userId)
                    call.respond(HttpStatusCode.Created, createdFood)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            put {
                val updateFood = call.receive<Food>()

                call.principalId()?.let { userId ->
                    val updatedFood = foodService.updateFood(food = updateFood, userId = userId)
                    call.respond(HttpStatusCode.OK, updatedFood)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            delete("/{id}") {
                val requestParam = call.parameters["id"]
                    ?.toString()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)

                val foodId = UUID.fromString(requestParam)

                call.principalId()?.let { userId ->
                    foodService.deleteFood(foodId = foodId, userId = userId)
                    call.respond(HttpStatusCode.NoContent)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
