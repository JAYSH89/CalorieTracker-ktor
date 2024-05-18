package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.models.Food
import nl.jaysh.services.FoodService
import org.koin.ktor.ext.inject
import java.util.*

fun Route.food() {

    val foodService by inject<FoodService>()

    route("/food") {
        get {
            val foods = foodService.getAllFood();
            call.respond(HttpStatusCode.OK, foods)
        }

        get("/{id}") {
            val id = call.parameters["id"]
                ?.toString()
                ?: throw IllegalStateException("Invalid id")

            val uuid = UUID.fromString(id)
            val food = foodService.getFoodById(id = uuid)

            if (food == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(HttpStatusCode.OK, food)
        }

        post {
            val food = call.receive<Food>()
            val createdFood = foodService.createFood(food = food)
            call.respond(HttpStatusCode.Created, createdFood)
        }

        put {
            val food = call.receive<Food>()
            val createdFood = foodService.updateFood(food = food)
            call.respond(HttpStatusCode.OK, createdFood)
        }

        delete("/{id}") {
            val id = call.parameters["id"]
                ?.toString()
                ?: throw IllegalStateException("Must provide id")

            val uuid = UUID.fromString(id)
            foodService.deleteFood(id = uuid)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
