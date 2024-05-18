package nl.jaysh.data

import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.toUser
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.helpers.objects.testUser
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.ds.PGSimpleDataSource
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserTests {
    private lateinit var userRepository: UserRepository

    private val dataSource = PGSimpleDataSource().apply {
        user = "postgres"
        password = "postgres"
        databaseName = "calorietracker"
        portNumbers = intArrayOf(5433)
    }

    private val database = Database.connect(datasource = dataSource)

    @BeforeTest
    fun resetDb() {
        transaction(db = database) {
            SchemaUtils.drop(UserTable)
            SchemaUtils.createMissingTablesAndColumns(UserTable)
        }

        userRepository = UserRepository()
    }

    @Test
    fun getAllUsers() {
        transaction(db = database) {
            UserTable.insert(testUser)
            val users = userRepository.getAll()

            assertEquals(users.size, 1)
        }
    }

    @Test
    fun findUserById() {
        transaction(db = database) {
            UserTable.insert(testUser)
        }

        transaction(db = database) {
            val userId = UserTable.getAll().first().id!!
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
    fun createUser() {
        transaction(db = database) {
            assertEquals(userRepository.getAll().size, 0)
        }

        transaction(db = database) {
            userRepository.createUser(user = testUser)

            val user = UserTable
                .getAll()
                .firstOrNull()

            assertNotNull(user)
            assertEquals(UserTable.getAll().size, 1)
            assertEquals(user.email, testUser.email)
        }
    }

    @Test
    fun `create user duplicate email should fail`() {
        transaction(db = database) {
            UserTable.insert(user = testUser)
        }

        transaction(db = database) {
            assertFailsWith<ExposedSQLException> {
                userRepository.createUser(user = testUser)
            }
        }
    }

    @Test
    fun `create user empty email address should fail`() {
        transaction(db = database) {
            assertFailsWith<ExposedSQLException> {
                userRepository.createUser(user = testUser.copy(email = ""))
            }
        }
    }

    @Test
    fun `create user empty password should fail`() {
        transaction(db = database) {
            assertFailsWith<ExposedSQLException> {
                userRepository.createUser(user = testUser.copy(password = ""))
            }
        }
    }

    @Test
    fun updateUser() {
        transaction(db = database) {
            UserTable.insert(testUser)
        }

        transaction(db = database) {
            val savedUser = UserTable.getAll().first()
            userRepository.updateUser(savedUser.copy(firstName = "Erik"))
        }

        transaction(db = database) {
            val updatedUser = UserTable.getAll().first()
            assertEquals(updatedUser.firstName, "Erik")
        }
    }

    @Test
    fun `update user invalid id should fail`() {
        transaction(db = database) {
            val invalidUser = testUser.copy(id = UUID.randomUUID())
            assertFailsWith<IllegalStateException> {
                userRepository.updateUser(invalidUser)
            }
        }
    }

    @Test
    fun `update user empty email address should fail`() {
        transaction(db = database) {
            UserTable.insert(testUser)
        }

        transaction(db = database) {
            val invalidUser = UserTable
                .selectAll()
                .first()
                .toUser()
                .copy(email = "")

            assertFailsWith<ExposedSQLException> {
                userRepository.updateUser(invalidUser)
            }
        }
    }

    @Test
    fun `update user empty password should fail`() {
        transaction(db = database) {
            UserTable.insert(testUser)
        }

        transaction(db = database) {
            val invalidUser = UserTable
                .selectAll()
                .first()
                .toUser()
                .copy(password = "")

            assertFailsWith<ExposedSQLException> {
                userRepository.updateUser(invalidUser)
            }
        }
    }

    @Test
    fun deleteUser() {
        transaction(db = database) {
            UserTable.insert(testUser)
        }

        transaction(db = database) {
            val userId = UserTable.getAll().first().id
            assertNotNull(userId)
            userRepository.deleteUser(userId)
            assertEquals(UserTable.getAll().size, 0)
        }
    }

    @Test
    fun `delete user invalid id should fail`() {
        transaction(db = database) {
            val invalidId = UUID.randomUUID()
            assertFailsWith<IllegalStateException> {
                userRepository.deleteUser(invalidId)
            }
        }
    }
}
