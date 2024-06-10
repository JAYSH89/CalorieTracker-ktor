package nl.jaysh.data.repositories

import nl.jaysh.data.db.ProfileTable
import nl.jaysh.data.db.delete
import nl.jaysh.data.db.findById
import nl.jaysh.data.db.insert
import nl.jaysh.models.user.Profile
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ProfileRepository {

    init {
        transaction { SchemaUtils.create(ProfileTable) }
    }

    fun findById(userId: UUID): Profile? = transaction {
        ProfileTable.findById(userId = userId)
    }

    fun save(profile: Profile, userId: UUID): Profile = transaction {
        ProfileTable.insert(profile = profile, userId = userId)
    }

    fun delete(userId: UUID) = transaction {
        ProfileTable.delete(userId = userId)
    }
}
