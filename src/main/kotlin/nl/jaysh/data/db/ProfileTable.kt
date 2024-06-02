package nl.jaysh.data.db

import nl.jaysh.models.Gender
import nl.jaysh.models.user.ActivityFactor
import nl.jaysh.models.user.Profile
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

object ProfileTable : Table(name = "profile") {

    val user: Column<EntityID<UUID>> = reference(
        name = "user",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.CASCADE,
    ).uniqueIndex()

    val firstName: Column<String?> = UserTable.varchar(name = "first_name", length = 50).nullable()

    val lastName: Column<String?> = UserTable.varchar(name = "last_name", length = 50).nullable()

    val dateOfBirth: Column<LocalDateTime> = datetime(name = "date_of_birth")

    val height: Column<Double> = double(name = "height")

    val gender: Column<Gender> = customEnumeration(
        name = "gender",
        sql = "ENUM('MALE', 'FEMALE', 'UNKNOWN')",
        fromDb = { value -> Gender.valueOf(value as String) },
        toDb = { it.name }
    )

    val activityFactor: Column<ActivityFactor> = customEnumeration(
        name = "activity_factor",
        sql = "ENUM('SEDENTARY', 'LIGHT_ACTIVITY', 'MODERATE_ACTIVITY', 'VIGOROUS_ACTIVITY')",
        fromDb = { value -> ActivityFactor.valueOf(value as String) },
        toDb = { it.name }
    )

    override val primaryKey = super.primaryKey ?: PrimaryKey(user)
}

fun ProfileTable.findById(userId: UUID): Profile? = selectAll()
    .where { user eq userId }
    .map(ResultRow::toProfile)
    .singleOrNull()

fun ProfileTable.insert(profile: Profile, userId: UUID): Profile {
    val result = upsert {
        it[user] = userId
        it[firstName] = profile.firstName
        it[lastName] = profile.lastName
        it[dateOfBirth] = profile.dateOfBirth
        it[height] = profile.height
        it[gender] = profile.gender
        it[activityFactor] = profile.activityFactor
    }
    check(result.insertedCount == 1)

    val updatedProfile = findById(userId = userId)
    requireNotNull(updatedProfile)

    return updatedProfile
}

fun ProfileTable.delete(userId: UUID) {
    val rowsChanged = deleteWhere { user eq userId }
    check(rowsChanged == 1)
}

fun ResultRow.toProfile(): Profile = Profile(
    firstName = this[ProfileTable.firstName],
    lastName = this[ProfileTable.lastName],
    dateOfBirth = this[ProfileTable.dateOfBirth],
    height = this[ProfileTable.height],
    gender = this[ProfileTable.gender],
    activityFactor = this[ProfileTable.activityFactor],
)
