package nl.jaysh.data.repositories

import nl.jaysh.data.db.JournalTable
import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.insert
import nl.jaysh.helpers.objects.testUser
import nl.jaysh.models.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class JournalTests {

    private lateinit var user: User
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
            SchemaUtils.create(JournalTable, UserTable)
            user = UserTable.insert(email = testUser.email, password = testUser.password)
        }

        journalRepository = JournalRepository()
    }

    @AfterTest
    fun teardown() {
        transaction(db = database) {
            SchemaUtils.drop(JournalTable, UserTable)
        }
    }

    @Test
    fun `findById successful should return JournalEntry`() {

    }

    @Test
    fun `findById not found should return null`() {

    }

    @Test
    fun `findById with userId not matches should return null`() {

    }

    @Test
    fun `getBetween should return list of journalEntries between startDate and endDate`() {

    }

    @Test
    fun `getBetween should return empty list if no journalEntries between startDate and endDate`() {

    }

    @Test
    fun `getBetween should return empty list userId not matches`() {

    }

    @Test
    fun `save should persist journalEntry`() {

    }

    @Test
    fun `save with invalid userId should throw exception`() {

    }

    @Test
    fun `delete should remove journalEntry`() {

    }

    @Test
    fun `delete with invalid userId should throw exception`() {

    }
}
