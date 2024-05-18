package nl.jaysh.data.repositories

import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.findById
import nl.jaysh.data.db.getAll
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.update
import nl.jaysh.models.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository {

    fun getAll(): List<User> = transaction {
        UserTable.getAll()
    }

    fun findById(id: UUID): User? = transaction {
        UserTable.findById(id = id)
    }

    fun createUser(user: User): User = transaction {
        UserTable.insert(user = user)
    }

    fun updateUser(user: User): User = transaction {
        UserTable.update(user = user)
    }

    fun deleteUser(id: UUID) = transaction {
        UserTable.delete(id = id)
    }
}
