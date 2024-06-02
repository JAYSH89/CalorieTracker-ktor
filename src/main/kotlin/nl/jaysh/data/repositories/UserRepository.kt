package nl.jaysh.data.repositories

import nl.jaysh.data.db.UserTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.findByEmail
import nl.jaysh.data.db.findById
import nl.jaysh.data.db.insert
import nl.jaysh.data.db.update
import nl.jaysh.models.user.User
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository {

    init {
        transaction { SchemaUtils.create(UserTable) }
    }

    fun findById(userId: UUID): User? = transaction {
        UserTable.findById(userId = userId)
    }

    fun findByEmail(email: String): User? = transaction {
        UserTable.findByEmail(email = email)
    }

    fun insert(email: String, password: String): User = transaction {
        UserTable.insert(email = email, password = password)
    }

    fun update(user: User): User = transaction {
        UserTable.update(user = user)
    }

    fun delete(userId: UUID) = transaction {
        UserTable.delete(userId = userId)
    }
}
