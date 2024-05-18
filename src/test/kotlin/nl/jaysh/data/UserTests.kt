package nl.jaysh.data

import nl.jaysh.data.db.UserTable
import nl.jaysh.data.repositories.UserRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.ds.PGSimpleDataSource
import kotlin.test.BeforeTest
import kotlin.test.Test

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

    }

    @Test
    fun findUserById() {

    }

    @Test
    fun `findUserById id not found`() {

    }

    @Test
    fun createUser() {

    }

    @Test
    fun `create user duplicate email should fail`() {

    }

    @Test
    fun `create user empty email address should fail`() {

    }

    @Test
    fun `create user empty password should fail`() {

    }

    @Test
    fun updateUser() {

    }

    @Test
    fun `update user invalid id should fail`() {

    }

    @Test
    fun `update user empty email address should fail`() {

    }

    @Test
    fun `update user empty password should fail`() {

    }

    @Test
    fun deleteUser() {

    }

    @Test
    fun `delete user invalid id should fail`() {

    }
}
