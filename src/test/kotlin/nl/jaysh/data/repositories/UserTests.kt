package nl.jaysh.data.repositories

import nl.jaysh.data.db.FoodTable
import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.toUser
import nl.jaysh.helpers.objects.testUser
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserTests {
    private lateinit var userRepository: UserRepository
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

        userRepository = UserRepository()
    }

    @AfterTest
    fun tearDown() {
        transaction(db = database) {
            SchemaUtils.drop(FoodTable, UserTable)
        }
    }

    @Test
    fun `findById should return User given an id`() {
        transaction(db = database) {
            UserTable.insert(email = testUser.email, password = testUser.password)

            val userId = UserTable.selectAll().first().toUser().id
            val user = userRepository.findById(userId)

            assertNotNull(user)
            assertEquals(user.email, testUser.email)
        }
    }

    @Test
    fun `findUserById id not found`() {
        transaction(db = database) {
            assertNull(userRepository.findById(UUID.randomUUID()))
        }
    }

    @Test
    fun `findUserByEmail should return User given email`() {
        transaction(db = database) {
            UserTable.insert(email = testUser.email, password = testUser.password)

            val savedUser = UserTable.selectAll().first().toUser()
            val user = userRepository.findByEmail(email = savedUser.email)

            assertNotNull(user)
            assertEquals(user.id, savedUser.id)
        }
    }

    @Test
    fun `findUserByEmail not found should return null`() {
        transaction(db = database) {
            assertNull(userRepository.findByEmail(email = "invalid@example.com"))
        }
    }

    @Test
    fun `create user should insert new user`() {
        transaction(db = database) {
            val initialUsers = UserTable.selectAll().map(ResultRow::toUser)
            assertEquals(initialUsers.size, 0)

            userRepository.insert(email = testUser.email, password = testUser.password)

            val user = UserTable
                .selectAll()
                .where { UserTable.email eq testUser.email }
                .map(ResultRow::toUser)
                .singleOrNull()

            val allUsers = UserTable.selectAll().map(ResultRow::toUser)
            assertNotNull(user)
            assertEquals(allUsers.size, 1)
        }
    }

    @Test
    fun `create user duplicate email should fail`() {
        transaction(db = database) {
            UserTable.insert(email = testUser.email, password = testUser.password)

            assertFailsWith<ExposedSQLException> {
                userRepository.insert(email = testUser.email, password = testUser.password)
            }
        }
    }

    @Test
    fun `create user empty email address should fail`() {
        transaction(db = database) {
            assertFailsWith<ExposedSQLException> {
                val invalidUser = testUser.copy(email = "")
                userRepository.insert(email = invalidUser.email, password = invalidUser.password)
            }
        }
    }

    @Test
    fun `create user empty password should fail`() {
        transaction(db = database) {
            assertFailsWith<ExposedSQLException> {
                val invalidUser = testUser.copy(password = "")
                userRepository.insert(email = invalidUser.email, password = invalidUser.password)
            }
        }
    }

    @Test
    fun `updateUser should update record successfully`() {
        transaction(db = database) {
            UserTable.insert(email = testUser.email, password = testUser.password)

            val savedUser = UserTable.selectAll().first().toUser()
            val changedUser = savedUser.copy(firstName = "Erik")
            userRepository.update(user = changedUser)

            val updatedUser = UserTable.selectAll().first().toUser()
            assertEquals(updatedUser.firstName, "Erik")
        }
    }

    @Test
    fun `update user invalid id should fail`() {
        transaction(db = database) {
            val invalidUser = testUser.copy(id = UUID.randomUUID())
            assertFailsWith<IllegalStateException> {
                userRepository.update(invalidUser)
            }
        }
    }

    @Test
    fun `update user empty email address should fail`() {
        transaction(db = database) {
            UserTable.insert(email = testUser.email, password = testUser.password)

            val invalidUser = UserTable
                .selectAll()
                .first()
                .toUser()
                .copy(email = "")

            assertFailsWith<ExposedSQLException> {
                userRepository.update(invalidUser)
            }
        }
    }

    @Test
    fun `update user empty password should fail`() {
        transaction(db = database) {
            UserTable.insert(email = testUser.email, password = testUser.password)

            val invalidUser = UserTable
                .selectAll()
                .first()
                .toUser()
                .copy(password = "")

            assertFailsWith<ExposedSQLException> {
                userRepository.update(invalidUser)
            }
        }
    }

    @Test
    fun `delete should remove user from db`() {
        transaction(db = database) {
            UserTable.insert(email = testUser.email, password = testUser.password)

            val userId = UserTable.selectAll().first().toUser().id
            assertNotNull(userId)

            userRepository.delete(userId)

            val allUsers = UserTable.selectAll().map(ResultRow::toUser)
            assertEquals(allUsers.size, 0)
        }
    }

    @Test
    fun `delete user invalid id should fail`() {
        transaction(db = database) {
            val invalidId = UUID.randomUUID()
            assertFailsWith<IllegalStateException> {
                userRepository.delete(invalidId)
            }
        }
    }
}
