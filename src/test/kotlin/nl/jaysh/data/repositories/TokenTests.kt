package nl.jaysh.data.repositories

import nl.jaysh.data.db.TokenTable
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

class TokenTests {
    private lateinit var user: User
    private lateinit var tokenRepository: TokenRepository
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
            SchemaUtils.create(TokenTable, UserTable)
            user = UserTable.insert(email = testUser.email, password = testUser.password)
        }

        tokenRepository = TokenRepository()
    }

    @AfterTest
    fun teardown() {
        transaction(db = database) {
            SchemaUtils.drop(TokenTable, UserTable)
        }
    }

    @Test
    fun `findToken successful should return RefreshToken`() {

    }

    @Test
    fun `findToken not found should return null`() {

    }

    @Test
    fun `save should persist token and user to db`() {

    }

    @Test
    fun `save should overwrite current token for user`() {

    }

    @Test
    fun `save with userId not found should throw IllegalStateException`() {

    }

    @Test
    fun `delete with valid userId should remove record from db`() {

    }
}
