package nl.jaysh.data.db

import nl.jaysh.models.RefreshToken
import nl.jaysh.models.User
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert
import java.time.LocalDateTime
import java.util.*

object TokenTable : Table(name = "refresh_token") {
    val token: Column<String> = text(name = "token").uniqueIndex()
    val issuedAt: Column<LocalDateTime> = datetime(name = "issued_at")
    val expiresAt: Column<LocalDateTime> = datetime(name = "expires_at")

    val user: Column<EntityID<UUID>> = reference(
        name = "user",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.CASCADE,
    ).uniqueIndex()

    override val primaryKey = super.primaryKey ?: PrimaryKey(user)
}

fun TokenTable.getRefreshToken(token: String): RefreshToken? = (TokenTable innerJoin UserTable)
    .selectAll()
    .where { TokenTable.token eq token }
    .mapNotNull { row ->
        val user = row.toUser()
        row.toRefreshToken(user)
    }
    .singleOrNull()

fun TokenTable.insert(token: RefreshToken, userId: UUID): RefreshToken {
    val result = upsert {
        it[TokenTable.token] = token.token
        it[TokenTable.issuedAt] = token.issuedAt
        it[TokenTable.expiresAt] = token.expiresAt
        it[TokenTable.user] = userId
    }
    check(result.insertedCount == 1)

    val savedToken = getRefreshToken(token = token.token)
    requireNotNull(savedToken)

    return savedToken
}

fun TokenTable.delete(userId: UUID) {
    val rowsChanged = deleteWhere { TokenTable.user eq userId }
    check(rowsChanged == 1)
}

fun ResultRow.toRefreshToken(user: User): RefreshToken = RefreshToken(
    token = this[TokenTable.token],
    issuedAt = this[TokenTable.issuedAt],
    expiresAt = this[TokenTable.expiresAt],
    user = user,
)
