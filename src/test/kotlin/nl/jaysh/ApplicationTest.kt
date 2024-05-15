package nl.jaysh

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import nl.jaysh.di.appModule
import nl.jaysh.models.AmountType
import nl.jaysh.models.Food
import nl.jaysh.plugins.configureRouting
import nl.jaysh.plugins.configureSerialization
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.*

class ApplicationTest : KoinTest {

    // This is dumb - figure out how to fix this with an object
    private val samplePayload = """
        {
            "id": 1,
            "name": "Egg",
            "carbs": 1.0,
            "proteins": 1.0,
            "fats": 1.0,
            "amount": 1.0,
            "amountType": "UNIT"
        }        
    """.trimIndent()

    val payload = Food(
        name = "Egg",
        carbs = 1.0,
        proteins = 1.0,
        fats = 1.0,
        amount = 1.0,
        amountType = AmountType.UNIT,
    )

    @Before
    fun setUp() {
        startKoin {
            modules(appModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test get all foods successful`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        val response = client.get("/food")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `test post food successful`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        val response = client.post("/food") {
            contentType(ContentType.Application.Json)
            setBody(samplePayload)
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }
}
