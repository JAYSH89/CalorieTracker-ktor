package nl.jaysh.data.repositories

import junit.framework.TestCase.assertNotNull
import nl.jaysh.data.db.FoodTable
import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.toFood
import nl.jaysh.data.db.toUser
import nl.jaysh.helpers.objects.testFood
import nl.jaysh.helpers.objects.testUser
import nl.jaysh.models.User
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

    private lateinit var user: User
    private lateinit var foodRepository: FoodRepository

    private val dataSource = PGSimpleDataSource().apply {
        user = "postgres"
        password = "postgres"
        databaseName = "calorietracker"
        portNumbers = intArrayOf(5433)
    }

    private val database = Database.connect(datasource = dataSource)

    @BeforeTest
    fun resetDb() {
        foodRepository = FoodRepository()
        transaction(database) {
            SchemaUtils.drop(FoodTable)
            SchemaUtils.drop(UserTable)
            SchemaUtils.createMissingTablesAndColumns(FoodTable)
            SchemaUtils.createMissingTablesAndColumns(UserTable)

            UserTable.insert(testUser)
            val savedUser = UserTable.selectAll().first().toUser()
            user = savedUser
        }
    }

    @Test
    fun `getAll should return empty list if no food`() {
        transaction(db = database) {
            val allFoods = foodRepository.getAll(userId = user.id!!)
            assertEquals(allFoods, emptyList())
        }
    }

    @Test
    fun `getAll should return list of food`() {
        transaction(db = database) {
            FoodTable.insert(food = testFood, userId = user.id!!)
            FoodTable.insert(food = testFood.copy(name = "Tomato"), userId = user.id!!)

            val allFoods = foodRepository.getAll(userId = user.id!!)
            assertEquals(allFoods.size, 2)
        }
    }

    @Test
    fun `findById should return Food given an id`() {
        transaction(db = database) {
            FoodTable.insert(food = testFood, userId = user.id!!)
            val foodId = FoodTable.getAll(userId = user.id!!).first().id!!

            assertNotNull(foodRepository.findById(foodId = foodId, userId = user.id!!))
        }
    }

    @Test
    fun `findById should return null if food not found`() {
        transaction(db = database) {
            assertNull(foodRepository.findById(foodId = UUID.randomUUID(), userId = user.id!!))
        }
    }

    @Test
    fun `create food should insert food in db`() {
        transaction(db = database) {
            assertEquals(foodRepository.getAll(userId = user.id!!).size, 0)

            foodRepository.insert(food = testFood, userId = user.id!!)

            assertEquals(FoodTable.getAll(userId = user.id!!).size, 1)
        }
    }

    @Test
    fun `update should update food in the db`() {
        transaction(db = database) {
            FoodTable.insert(food = testFood, userId = user.id!!)

            val changedFood = FoodTable
                .getAll(userId = user.id!!)
                .first()
                .copy(name = "changed food")

            foodRepository.update(food = changedFood, userId = user.id!!)

            val updatedFood = FoodTable.selectAll().first().toFood()
            assertEquals(updatedFood.name, changedFood.name)
        }
    }

    @Test
    fun `update invalid id should throw IllegalStateException`() {
        transaction(db = database) {
            val invalidFood = testFood.copy(id = UUID.randomUUID())
            assertFailsWith<IllegalStateException> {
                foodRepository.update(food = invalidFood, userId = user.id!!)
            }
        }
    }

    @Test
    fun `delete with id successful`() {
        transaction(db = database) {
            FoodTable.insert(food = testFood, userId = user.id!!)
            FoodTable.insert(food = testFood.copy(name = "Apple"), userId = user.id!!)

            val id = FoodTable.getAll(userId = user.id!!).first().id!!
            foodRepository.delete(foodId = id, userId = user.id!!)

            assertEquals(FoodTable.getAll(userId = user.id!!).size, 1)
        }
    }

    @Test
    fun `delete with invalid id should throw IllegalStateException`() {
        transaction(db = database) {
            val invalidId = UUID.randomUUID()
            assertFailsWith<IllegalStateException> {
                foodRepository.delete(foodId = invalidId, userId = user.id!!)
            }
        }
    }
}

