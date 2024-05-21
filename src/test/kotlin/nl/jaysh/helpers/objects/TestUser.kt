package nl.jaysh.helpers.objects

import nl.jaysh.models.Gender
import nl.jaysh.models.User
import java.time.LocalDateTime
import java.util.*

val testUser = User(
    id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
    email = "johndoe@example.com",
    password = "testPass123",
    firstName = "John",
    lastName = "Doe",
    birthday = LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
    gender = Gender.MALE,
)
