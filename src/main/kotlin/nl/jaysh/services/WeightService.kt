package nl.jaysh.services

import nl.jaysh.data.repositories.WeightRepository
import nl.jaysh.models.weight.Weight
import java.util.UUID

class WeightService(private val weightRepository: WeightRepository) {

    fun getAll(userId: UUID): List<Weight> {
        return weightRepository.getAll(userId = userId)
    }

    fun insert(weight: Weight, userId: UUID): Weight {
        return weightRepository.insert(weightEntry = weight, userId = userId)
    }

    fun update(weight: Weight, userId: UUID): Weight {
        return weightRepository.update(weightEntry = weight, userId = userId)
    }

    fun delete(weightId: UUID, userId: UUID) {
        weightRepository.delete(weightId = weightId, userId = userId)
    }
}
