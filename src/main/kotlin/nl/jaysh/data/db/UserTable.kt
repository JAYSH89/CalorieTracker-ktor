package nl.jaysh.data.db

import nl.jaysh.models.Gender
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

    init {
        check(name = "email_not_empty") { email neq "" }
        check(name = "password_not_empty") { password neq "" }
    }
}

fun ResultRow.toUser() = User(
    id = this[UserTable.id].value,
    email = this[UserTable.email],
    password = this[UserTable.password],
    firstName = this[UserTable.firstName],
    lastName = this[UserTable.lastName],
    birthday = this[UserTable.birthday],
    gender = this[UserTable.gender]
        ?.let { Gender.fromString(it) }
        ?: Gender.UNKNOWN,
)

fun UserTable.findById(userId: UUID): User? = selectAll()
    .where { UserTable.id eq userId }
    .map(ResultRow::toUser)
    .singleOrNull()

fun UserTable.findByEmail(email: String): User? = selectAll()
    .where { UserTable.email eq email }
    .map(ResultRow::toUser)
    .singleOrNull()

fun UserTable.insert(email: String, password: String): User {
    val id = insertAndGetId {
        it[UserTable.email] = email
        it[UserTable.password] = password
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
        it[gender] = user.gender.toString()
        it[updatedAt] = LocalDateTime.now()
    }
    check(rowsChanged == 1)

    val updatedUser = findById(user.id)
    requireNotNull(updatedUser)

    return updatedUser
}

fun UserTable.delete(userId: UUID) {
    val rowsChanged = deleteWhere { UserTable.id eq userId }
    check(rowsChanged == 1)
}
