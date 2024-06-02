package nl.jaysh.data.repositories

import nl.jaysh.data.db.TokenTable
import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.getRefreshToken
import nl.jaysh.data.db.insert
import nl.jaysh.helpers.objects.testRefreshToken
import nl.jaysh.helpers.objects.testUser
import nl.jaysh.models.user.User
import org.jetbrains.exposed.exceptions.ExposedSQLException
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
        transaction(db = database) {
            val token = TokenTable.insert(token = testRefreshToken, userId = user.id)

            val result = tokenRepository.findToken(token = token.token)
            assertNotNull(result)
            assertEquals(result, token)
        }
    }

    @Test
    fun `findToken not found should return null`() {
        transaction(db = database) {
            val token = TokenTable.insert(token = testRefreshToken, userId = user.id)

            val nonExistingToken = "does not exist"
            val result = tokenRepository.findToken(token = nonExistingToken)
            assertNull(result)
        }
    }

    @Test
    fun `save should persist token and user to db`() {
        transaction(db = database) {
            val token = tokenRepository.save(testRefreshToken, user.id)

            val result = TokenTable.getRefreshToken(token = token.token)
            assertNotNull(result)
            assertEquals(result, token)
        }
    }

    @Test
    fun `save should overwrite current token for user`() {
        transaction(db = database) {
            val token = TokenTable.insert(token = testRefreshToken, userId = user.id)

            val updatedToken = token.copy(token = "updated token")
            tokenRepository.save(updatedToken, userId = user.id)

            val result = TokenTable.getRefreshToken(token = updatedToken.token)
            assertNotNull(result)
            assertEquals(result, updatedToken)
            assertNull(TokenTable.getRefreshToken(token = token.token))
        }
    }

    @Test
    fun `save with userId not found should throw IllegalStateException`() {
        transaction(db = database) {
            val invalidUser = user.copy(id = UUID.randomUUID())

            assertFailsWith<ExposedSQLException> {
                tokenRepository.save(testRefreshToken, userId = invalidUser.id)
            }
        }
    }

    @Test
    fun `delete with valid userId should remove record from db`() {
        transaction(db = database) {
            TokenTable.insert(token = testRefreshToken, userId = user.id)

            tokenRepository.delete(userId = user.id)

            val result = TokenTable.getRefreshToken(testRefreshToken.token)
            assertNull(result)
        }
    }

    @Test
    fun `delete with invalid userId should fail with exception`() {
        transaction(db = database) {
            TokenTable.insert(token = testRefreshToken, userId = user.id)

            val invalidUser = user.copy(id = UUID.randomUUID())
            assertFailsWith<IllegalStateException> {
                tokenRepository.delete(userId = invalidUser.id)
            }

            assertNotNull(TokenTable.getRefreshToken(testRefreshToken.token))
        }
    }
}
