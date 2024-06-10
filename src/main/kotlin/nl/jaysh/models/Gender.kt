package nl.jaysh.models

enum class Gender {
    MALE,
    FEMALE,
    UNKNOWN;

    companion object {
        fun fromString(value: String): Gender = entries
            .firstOrNull { gender -> value == gender.toString() }
            ?: UNKNOWN
    }
}
