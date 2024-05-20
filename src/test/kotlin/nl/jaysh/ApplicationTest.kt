package nl.jaysh

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import nl.jaysh.data.db.FoodTable
import nl.jaysh.data.db.UserTable
import nl.jaysh.plugins.configureKoin
import nl.jaysh.plugins.configureRouting
import nl.jaysh.plugins.configureSecurity
import nl.jaysh.plugins.configureSerialization
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    private lateinit var database: Database

    @BeforeTest
    fun setup() {
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )

        transaction(database) {
            SchemaUtils.create(FoodTable, UserTable)
        }
    }

    @AfterTest
    fun tearDown() {
        transaction(db = database) {
            SchemaUtils.drop(FoodTable, UserTable)
        }
    }

    @Test
    fun launchApplicationTest() = testApplication {
        application {
            configureKoin()
            configureSerialization()
            configureSecurity()
            configureRouting()
        }

        environment {
            config = ApplicationConfig(configPath = "application-test.conf")
        }

        val response = client.get("/json/kotlinx-serialization")
        assertEquals(response.status, HttpStatusCode.OK)
    }
}
