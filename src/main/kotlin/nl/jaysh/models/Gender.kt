package nl.jaysh.models

enum class Gender {
    MALE,
    FEMALE;

    companion object {
        fun fromString(value: String): Gender = entries
            .find { it.toString().uppercase() == value.uppercase() }
            ?: throw IllegalArgumentException("Unknown gender '$value'")
    }
}
