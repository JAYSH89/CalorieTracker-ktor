package nl.jaysh.data.repositories

import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.WeightTable
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.helpers.objects.testUser
import nl.jaysh.helpers.objects.testWeight
import nl.jaysh.models.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class WeightTests {
    private lateinit var user: User
    private lateinit var weightRepository: WeightRepository
    private lateinit var database: Database

    @BeforeTest
    fun setup() {
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )

        transaction {
            SchemaUtils.create(WeightTable, UserTable)
            user = UserTable.insert(email = testUser.email, password = testUser.password)
        }

        weightRepository = WeightRepository()
    }

    @AfterTest
    fun teardown() {
        transaction(db = database) {
            SchemaUtils.drop(WeightTable, UserTable)
        }
    }

    @Test
    fun `getAll should return list of weight successful`() {
        transaction(db = database) {
            WeightTable.insert(weightEntry = testWeight, userId = user.id)
            WeightTable.insert(weightEntry = testWeight, userId = user.id)

            val result = weightRepository.getAll(userId = user.id)

            assertEquals(result.size, 2)
        }
    }

    @Test
    fun `getAll should return empty list if no weight`() {
        transaction(db = database) {
            val result = weightRepository.getAll(userId = user.id)

            assert(result.isEmpty())
        }
    }

    @Test
    fun `getAll should return only items of user`() {
        transaction(db = database) {
            val anotherUser = UserTable.insert(email = "anotherUser@example.com", password = "testPass123")
            WeightTable.insert(weightEntry = testWeight, userId = anotherUser.id)

            val result = weightRepository.getAll(userId = user.id)

            assert(result.isEmpty())
        }
    }

    @Test
    fun `findById should return single weight element successful`() {
        transaction(db = database) {
            val savedWeight = WeightTable.insert(weightEntry = testWeight, userId = user.id)
            val result = weightRepository.findById(savedWeight.id!!, userId = user.id)

            assertNotNull(result)
            assertEquals(result.weight, testWeight.weight)
        }
    }

    @Test
    fun `findById should return null if weight not found`() {
        transaction(db = database) {
            val weightById = weightRepository.findById(weightId = UUID.randomUUID(), userId = user.id)

            assertNull(weightById)
        }
    }

    @Test
    fun `findById should return null if wrong userId`() {
        transaction(db = database) {
            val savedWeight = WeightTable.insert(weightEntry = testWeight, userId = user.id)
            val anotherUser = UserTable.insert(email = "anotherUser@example.com", password = "testPass123")

            val result = weightRepository.findById(weightId = savedWeight.id!!, userId = anotherUser.id)

            assertNull(result)
        }
    }

    @Test
    fun `insert should persist weight in DB`() {
        transaction(db = database) {
            assertEquals(WeightTable.getAll(userId = user.id).size, 0)

            val weight = weightRepository.insert(weightEntry = testWeight, userId = user.id)

            assertEquals(WeightTable.getAll(userId = user.id).size, 1)
        }
    }

    @Test
    fun `update should change weight in DB`() {
        transaction(db = database) {
            val savedWeight = WeightTable.insert(weightEntry = testWeight, userId = user.id)

            val update = savedWeight.copy(weight = 2.0)
            val updatedSavedWeight = weightRepository.update(update, userId = user.id)

            assertEquals(expected = updatedSavedWeight.id!!, actual = savedWeight.id!!)
            assertEquals(expected = updatedSavedWeight.weight, actual = 2.0)
        }
    }

    @Test
    fun `update with invalid userId should throw exception`() {
        transaction(db = database) {
            val savedWeight = WeightTable.insert(weightEntry = testWeight, userId = user.id)

            val update = savedWeight.copy(weight = 2.0)
            val invalidUser = user.copy(id = UUID.randomUUID())

            assertFailsWith<IllegalStateException> {
                weightRepository.update(weightEntry = update, userId = invalidUser.id)
            }
        }
    }

    @Test
    fun `delete with id successful`() {
        transaction(db = database) {
            val savedWeight = WeightTable.insert(weightEntry = testWeight, userId = user.id)
            weightRepository.delete(savedWeight.id!!, userId = user.id)

            assert(WeightTable.getAll(userId = user.id).isEmpty())
        }
    }

    @Test
    fun `delete with invalid id should throw IllegalStateException`() {
        transaction(db = database) {
            WeightTable.insert(weightEntry = testWeight, userId = user.id)
            val invalidId = UUID.randomUUID()

            assertFailsWith<IllegalStateException> {
                weightRepository.delete(invalidId, userId = user.id)
            }
        }
    }
}
