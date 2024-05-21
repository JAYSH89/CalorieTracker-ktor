package nl.jaysh.models

enum class Gender {
    MALE,
    FEMALE,
    UNKNOWN;

    companion object {
        fun fromString(value: String): Gender = entries
            .find { it.toString().uppercase() == value.uppercase() }
            ?: UNKNOWN
    }
}
