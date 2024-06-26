package nl.jaysh.models

enum class AmountType {
    UNIT,
    METRIC;

    companion object {
        fun fromString(value: String): AmountType = entries
            .find { it.toString().uppercase() == value.uppercase() }
            ?: throw IllegalArgumentException("Unknown amount type '$value'")
    }
}
