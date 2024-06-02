package nl.jaysh.data.repositories

import nl.jaysh.data.db.ProfileTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class ProfileRepository {

    init {
        transaction { SchemaUtils.create(ProfileTable) }
    }


}
