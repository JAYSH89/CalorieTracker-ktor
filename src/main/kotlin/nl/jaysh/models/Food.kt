package nl.jaysh.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.jaysh.core.utils.UUIDSerializer
import java.util.UUID

@Serializable
data class Food(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val name: String,
    val carbs: Double,
    val proteins: Double,
    val fats: Double,
    val amount: Double,
    @SerialName("amount_type") val amountType: AmountType,
)
