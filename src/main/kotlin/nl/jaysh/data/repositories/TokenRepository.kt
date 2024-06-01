package nl.jaysh.data.repositories

import nl.jaysh.data.db.TokenTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.getRefreshToken
import nl.jaysh.data.db.insert
import nl.jaysh.models.RefreshToken
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TokenRepository {

    init {
        transaction { SchemaUtils.create(TokenTable) }
    }

    fun findToken(token: String): RefreshToken? = transaction {
        TokenTable.getRefreshToken(token = token)
    }

    fun save(token: RefreshToken, userId: UUID): RefreshToken = transaction {
        TokenTable.insert(token = token, userId = userId)
    }

    fun delete(userId: UUID) = transaction {
        TokenTable.delete(userId = userId)
    }
}
