package nl.jaysh.data.repositories

import nl.jaysh.data.db.TokenTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.findUserByToken
import nl.jaysh.data.db.insert
import nl.jaysh.models.RefreshToken
import nl.jaysh.models.User
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TokenRepository {

    init {
        transaction { SchemaUtils.create(TokenTable) }
    }

    fun findToken(token: String): RefreshToken? = transaction {
        TokenTable.findUserByToken(token = token)
    }

    fun save(token: RefreshToken, user: User) = transaction {
        TokenTable.insert(token = token, userId = user.id)
    }

    fun delete(userId: UUID) = transaction {
        TokenTable.delete(userId = userId)
    }
}
