package nl.jaysh.models

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: Long? = null,
    val name: String,
    val carbs: Double,
    val proteins: Double,
    val fats: Double,
    val amount: Double,
    val amountType: AmountType,
)
