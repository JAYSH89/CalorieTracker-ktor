package nl.jaysh.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.jaysh.core.utils.principalId
import nl.jaysh.models.JournalEntry
import nl.jaysh.services.JournalService
import org.koin.ktor.ext.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Route.journal() {
    val journalService by inject<JournalService>()

    route("/api/journal") {
        authenticate {
            get("/{id}") {
                val requestParam = call.parameters["id"]
                    ?.toString()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                val journalEntryId = UUID.fromString(requestParam)

                call.principalId()?.let { userId ->
                    val journalEntry = journalService.findById(journalEntryId = journalEntryId, userId = userId)

                    if (journalEntry == null)
                        call.respond(HttpStatusCode.NotFound)
                    else
                        call.respond(HttpStatusCode.OK, journalEntry)

                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            get {
                call.principalId()?.let { userId ->
                    val startDateParam = call.request.queryParameters["startDate"]
                    val endDateParam = call.request.queryParameters["endDate"]

                    if (startDateParam != null && endDateParam != null) {
                        val startDate = LocalDateTime.parse(startDateParam, DateTimeFormatter.ISO_DATE_TIME)
                        val endDate = LocalDateTime.parse(endDateParam, DateTimeFormatter.ISO_DATE_TIME)

                        val result = journalService.getBetween(
                            startDate = startDate,
                            endDate = endDate,
                            userId = userId,
                        )
                        call.respond(result)

                        return@get
                    }

                    val result = journalService.getAllJournalEntries(userId = userId)
                    call.respond(result)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            post {
                val createJournalEntry = call.receive<JournalEntry>()

                call.principalId()?.let { userId ->
                    val createdFood = journalService.save(createJournalEntry, userId = userId)
                    call.respond(HttpStatusCode.Created, createdFood)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }

        authenticate {
            delete("/{id}") {
                val requestParam = call.parameters["id"]
                    ?.toString()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)

                val journalEntryId = UUID.fromString(requestParam)

                call.principalId()?.let { userId ->
                    journalService.delete(journalEntryId = journalEntryId, userId = userId)
                    call.respond(HttpStatusCode.NoContent)
                } ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
