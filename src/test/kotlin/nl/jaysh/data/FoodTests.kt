package nl.jaysh.data

import junit.framework.TestCase.assertNotNull
import nl.jaysh.data.db.FoodTable
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.toFood
import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.helpers.objects.testFood
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.ds.PGSimpleDataSource
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class FoodTests {

    private lateinit var foodRepository: FoodRepository

    private val dataSource = PGSimpleDataSource().apply {
        user = "postgres"
        password = "postgres"
        databaseName = "calorietracker"
    }

    private val database = Database.connect(datasource = dataSource)

    @BeforeTest
    fun resetDb() {
        transaction(database) {
            SchemaUtils.drop(FoodTable)
            SchemaUtils.createMissingTablesAndColumns(FoodTable)
        }

        foodRepository = FoodRepository()
    }

    @Test
    fun `add food`() {
        transaction(db = database) {
            assertEquals(foodRepository.getAll().size, 0)
        }

        transaction(db = database) {
            foodRepository.add(testFood)
            assertEquals(FoodTable.getAll().size, 1)
        }
    }

    @Test
    fun findById() {
        transaction(db = database) {
            FoodTable.insert(testFood)
        }

        transaction(db = database) {
            val uuid = FoodTable.getAll().first().id!!
            assertNotNull(foodRepository.findById(uuid))
        }
    }

    @Test
    fun `findById not found`() {
        transaction(db = database) {
            FoodTable.insert(testFood)
        }

        transaction(db = database) {
            assertNull(foodRepository.findById(UUID.randomUUID()))
        }
    }

    @Test
    fun update() {
        transaction(db = database) {
            FoodTable.insert(testFood)
        }

        transaction(db = database) {
            val changedFood = FoodTable
                .getAll()
                .first()
                .copy(name = "changed food")

            foodRepository.update(changedFood)

            val updatedFood = FoodTable.selectAll().first().toFood()
            assertEquals(updatedFood.name, changedFood.name)
        }
    }

    @Test
    fun `update invalid id`() {
        transaction(db = database) {
            FoodTable.insert(testFood)
        }

        transaction(db = database) {
            val invalidFood = FoodTable
                .getAll()
                .first()
                .copy(id = UUID.randomUUID())

            assertFailsWith<IllegalStateException> {
                foodRepository.update(invalidFood)
            }
        }
    }

    @Test
    fun `delete with id successful`() {
        transaction(db = database) {
            FoodTable.insert(testFood)
            FoodTable.insert(testFood.copy(name = "Apple"))
        }

        transaction(db = database) {
            val id = FoodTable.getAll().first().id!!
            foodRepository.delete(id = id)
        }

        transaction(db = database) {
            assertEquals(FoodTable.getAll().size, 1)
        }
    }

    @Test
    fun `delete with invalid id`() {
        val foods = FoodRepository()

        transaction(db = database) {
            val invalidId = UUID.randomUUID()
            assertFailsWith<IllegalStateException> {
                foods.delete(id = invalidId)
            }
        }
    }
}

