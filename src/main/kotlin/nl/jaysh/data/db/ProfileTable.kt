package nl.jaysh.data.db

import nl.jaysh.models.Gender
import nl.jaysh.models.user.ActivityFactor
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object ProfileTable : UUIDTable() {

    val dateOfBirth = datetime(name = "date_of_birth")

    val height = double(name = "height")

    val gender = customEnumeration(
        name = "gender",
        sql = "ENUM('MALE', 'FEMALE', 'UNKNOWN')",
        fromDb = { value -> Gender.valueOf(value as String) },
        toDb = { it.name }
    )

    val activityFactor = customEnumeration(
        name = "activity_factor",
        sql = "ENUM('SEDENTARY', 'LIGHT_ACTIVITY', 'MODERATE_ACTIVITY', 'VIGOROUS_ACTIVITY')",
        fromDb = { value -> ActivityFactor.valueOf(value as String) },
        toDb = { it.name }
    )

    val user: Column<EntityID<UUID>> = reference(
        name = "user",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.CASCADE,
    )
}
