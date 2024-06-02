package nl.jaysh.models.journal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    val journal: List<JournalEntry>,

    @SerialName("total_carbs")
    val totalCarbs: Double,

    @SerialName("total_proteins")
    val totalProteins: Double,

    @SerialName("total_fats")
    val totalFats: Double,

    @SerialName("calories_from_carbs")
    val caloriesFromCarbs: Double,

    @SerialName("calories_from_proteins")
    val caloriesFromProteins: Double,

    @SerialName("calories_from_fats")
    val caloriesFromFats: Double,

    @SerialName("total_calories")
    val totalCalories: Double,
)
