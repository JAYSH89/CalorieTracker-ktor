package nl.jaysh.models.user

enum class ActivityFactor {
    SEDENTARY,
    LIGHT_ACTIVITY,
    MODERATE_ACTIVITY,
    VIGOROUS_ACTIVITY,
    UNKNOWN;

    companion object {
        fun fromString(value: String): ActivityFactor = entries
            .firstOrNull { activityFactor -> value == activityFactor.toString() }
            ?: UNKNOWN
    }
}
