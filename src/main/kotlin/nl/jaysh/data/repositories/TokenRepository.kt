package nl.jaysh.data.repositories

import nl.jaysh.models.User

class TokenRepository {
    private val tokens = mutableMapOf<String, User>()

    fun findUserByToken(token: String): User? = tokens[token]

    fun save(token: String, user: User) {
        tokens[token] = user
    }

    // TODO: Add field for isValid
    fun tokenExists(token: String): Boolean = tokens.containsKey(token)
}
