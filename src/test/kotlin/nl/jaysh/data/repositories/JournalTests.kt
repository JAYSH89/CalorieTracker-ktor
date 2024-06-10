package nl.jaysh.data.repositories

import nl.jaysh.data.db.FoodTable
import nl.jaysh.data.db.JournalTable
import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.helpers.objects.TestJournalEntry
import nl.jaysh.helpers.objects.testFood
import nl.jaysh.helpers.objects.testUser
import nl.jaysh.models.food.Food
import nl.jaysh.models.user.User
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JournalTests {

    private lateinit var user: User
    private lateinit var food: Food
    private lateinit var journalRepository: JournalRepository
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
            SchemaUtils.create(JournalTable, UserTable, FoodTable)
            user = UserTable.insert(email = testUser.email, password = testUser.password)
            food = FoodTable.insert(food = testFood, userId = user.id)
        }

        journalRepository = JournalRepository()
    }

    @AfterTest
    fun teardown() {
        transaction(db = database) {
            SchemaUtils.drop(JournalTable, UserTable, FoodTable)
        }
    }

    @Test
    fun `getAll successful should return list of JournalEntry`() {
        transaction(db = database) {
            assertEquals(JournalTable.getAll(userId = user.id).size, 0)

            val journalEntry = TestJournalEntry.default(food = food)
            JournalTable.insert(journalEntry = journalEntry, userId = user.id)
            JournalTable.insert(journalEntry = journalEntry, userId = user.id)

            val result = journalRepository.getAll(userId = user.id)
            assertEquals(result.size, 2)
        }
    }

    @Test
    fun `getAll successful should return empty list of JournalEntry if not entries`() {
        transaction(db = database) {
            val result = journalRepository.getAll(userId = user.id)
            assert(result.isEmpty())
        }
    }

    @Test
    fun `getAll should return empty list of JournalEntry if not for this user`() {
        transaction(db = database) {
            val journalEntry = TestJournalEntry.default(food = food)
            JournalTable.insert(journalEntry = journalEntry, userId = user.id)
            assertEquals(JournalTable.getAll(userId = user.id).size, 1)

            val additionalUser = UserTable.insert(email = "test@example.com", password = "testPass123")
            val result = journalRepository.getAll(userId = additionalUser.id)
            assert(result.isEmpty())
        }
    }

    @Test
    fun `findById successful should return JournalEntry`() {
        transaction(db = database) {
            val journalEntry = TestJournalEntry.default(food = food)
            val newEntry = JournalTable.insert(journalEntry = journalEntry, userId = user.id)

            val result = journalRepository.findById(newEntry.id!!, userId = user.id)
            assertNotNull(result)
            assertEquals(result.food, newEntry.food)
        }
    }

    @Test
    fun `findById not found should return null`() {
        transaction(db = database) {
            val result = journalRepository.findById(UUID.randomUUID(), userId = user.id)
            assertNull(result)
        }
    }

    @Test
    fun `findById with userId not matches should return null`() {
        transaction(db = database) {
            val journalEntry = TestJournalEntry.default(food = food)
            val newEntry = JournalTable.insert(journalEntry = journalEntry, userId = user.id)

            val invalidUser = user.copy(id = UUID.randomUUID())
            val result = journalRepository.findById(newEntry.id!!, userId = invalidUser.id)

            assertNull(result)
        }
    }

    @Test
    fun `getBetween should return list of journalEntries between startDate and endDate`() {
        transaction(db = database) {
            val journalEntry = TestJournalEntry.default(food = food)
            JournalTable.insert(journalEntry = journalEntry, userId = user.id)

            val differentDate = LocalDateTime.of(2024, 6, 1, 0, 0, 0, 0)
            val newEntry = JournalTable.insert(journalEntry = journalEntry.copy(date = differentDate), userId = user.id)

            val end = differentDate.plusDays(1)
            val result = journalRepository.getBetween(
                startDate = differentDate,
                endDate = end,
                userId = user.id,
            )

            assertEquals(result, listOf(newEntry))
            assertEquals(JournalTable.getAll(userId = user.id).size, 2)
        }
    }

    @Test
    fun `getBetween should return empty list if no journalEntries between startDate and endDate`() {
        transaction(db = database) {
            val journalEntry = TestJournalEntry.default(food = food)
            JournalTable.insert(journalEntry = journalEntry, userId = user.id)

            val result = journalRepository.getBetween(
                startDate = journalEntry.date.plusYears(1),
                endDate = journalEntry.date.plusYears(2),
                userId = user.id,
            )

            assert(result.isEmpty())
        }
    }

    @Test
    fun `getBetween should return empty list userId not matches`() {
        transaction(db = database) {
            val journalEntry = TestJournalEntry.default(food = food)
            JournalTable.insert(journalEntry = journalEntry, userId = user.id)

            val invalidUser = user.copy(id = UUID.randomUUID())
            val result = journalRepository.getBetween(
                startDate = journalEntry.date.minusDays(1),
                endDate = journalEntry.date.plusDays(1),
                userId = invalidUser.id,
            )

            assert(result.isEmpty())
        }
    }

    @Test
    fun `insert should persist journalEntry in db`() {
        transaction(db = database) {
            val journalEntry = TestJournalEntry.default(food = food)
            JournalTable.insert(journalEntry = journalEntry, userId = user.id)

            assertEquals(journalRepository.getAll(userId = user.id).size, 1)
        }
    }

    @Test
    fun `save with invalid userId should throw exception`() {
        transaction(db = database) {
            val invalidUser = user.copy(id = UUID.randomUUID())

            val journalEntry = TestJournalEntry.default(food = food)
            assertFailsWith<ExposedSQLException> {
                journalRepository.insert(journalEntry = journalEntry, userId = invalidUser.id)
            }
        }
    }

    @Test
    fun `delete should remove journalEntry`() {
        transaction(db = database) {
            assertEquals(JournalTable.getAll(userId = user.id).size, 0)
            val journalEntry = TestJournalEntry.default(food = food)
            val newJournalEntryId = JournalTable.insert(journalEntry = journalEntry, userId = user.id).id

            assertEquals(JournalTable.getAll(userId = user.id).size, 1)

            journalRepository.delete(journalEntryId = newJournalEntryId!!, userId = user.id)

            assertEquals(JournalTable.getAll(userId = user.id).size, 0)
        }
    }

    @Test
    fun `delete with invalid userId should throw exception`() {
        transaction(db = database) {
            assertEquals(JournalTable.getAll(userId = user.id).size, 0)

            val journalEntry = TestJournalEntry.default(food = food)
            val newJournalEntryId = JournalTable.insert(journalEntry = journalEntry, userId = user.id).id
            assertEquals(JournalTable.getAll(userId = user.id).size, 1)

            assertFailsWith<IllegalStateException> {
                val invalidUser = user.copy(id = UUID.randomUUID())
                journalRepository.delete(
                    journalEntryId = newJournalEntryId!!,
                    userId = invalidUser.id,
                )
            }
            assertEquals(JournalTable.getAll(userId = user.id).size, 1)
        }
    }
}
