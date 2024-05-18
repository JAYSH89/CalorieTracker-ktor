package nl.jaysh.data.db

import nl.jaysh.models.User
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.util.*

object UserTable : UUIDTable() {
    val email: Column<String> = varchar(name = "email", length = 100).uniqueIndex()
    val password: Column<String> = varchar(name = "password", length = 60)
    val firstName: Column<String?> = varchar(name = "first_name", length = 50).nullable()
    val lastName: Column<String?> = varchar(name = "last_name", length = 50).nullable()
    val birthday: Column<LocalDateTime?> = datetime(name = "birthday").nullable()
    val gender: Column<String?> = varchar(name = "gender", length = 50).nullable()
    val createdAt: Column<LocalDateTime?> = datetime(name = "created_at").nullable()
    val updatedAt: Column<LocalDateTime?> = datetime(name = "updated_at").nullable()
}

fun ResultRow.toUser() = User(
    id = this[UserTable.id].value,
    email = this[UserTable.email],
    password = this[UserTable.password],
    firstName = this[UserTable.firstName],
    lastName = this[UserTable.lastName],
    birthday = this[UserTable.birthday],
    gender = this[UserTable.gender],
)

fun UserTable.getAll(): List<User> = selectAll()
    .map { entity -> entity.toUser() }

fun UserTable.findById(id: UUID): User? = selectAll()
    .where { UserTable.id eq id }
    .map { entity -> entity.toUser() }
    .singleOrNull()

fun UserTable.insert(user: User): User {
    val id = insertAndGetId {
        it[email] = user.email
        it[password] = user.password
        it[firstName] = user.firstName
        it[lastName] = user.lastName
        it[birthday] = user.birthday
        it[gender] = user.gender
        it[createdAt] = LocalDateTime.now()
        it[updatedAt] = LocalDateTime.now()
    }.value

    val newUser = findById(id)
    requireNotNull(newUser)

    return newUser
}

fun UserTable.update(user: User): User {
    requireNotNull(user.id)

    val rowsChanged = update({ UserTable.id eq user.id }) {
        it[email] = user.email
        it[password] = user.password
        it[firstName] = user.firstName
        it[lastName] = user.lastName
        it[birthday] = user.birthday
        it[gender] = user.gender
        it[updatedAt] = LocalDateTime.now()
    }
    check(rowsChanged == 1)

    val updatedUser = findById(user.id)
    requireNotNull(updatedUser)

    return updatedUser
}

fun UserTable.delete(id: UUID) {
    val rowsChanged = deleteWhere { UserTable.id eq id }
    check(rowsChanged == 1)
}
