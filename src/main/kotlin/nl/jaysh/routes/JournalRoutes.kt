package nl.jaysh.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import nl.jaysh.services.JournalService
import org.koin.ktor.ext.inject

fun Route.journal() {

    val journalService by inject<JournalService>()

    route("/api/journal") {
        authenticate {
            get("/{id}") {

            }
        }

        authenticate {
            get {
                val startDateParam = call.request.queryParameters["startDate"]
                val endDateParam = call.request.queryParameters["endDate"]
            }
        }

        authenticate {
            post {

            }
        }

        authenticate {
            delete("/{id}") {

            }
        }
    }
}
