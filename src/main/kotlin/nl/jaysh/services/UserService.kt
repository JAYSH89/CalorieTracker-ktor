package nl.jaysh.services

import nl.jaysh.data.repositories.ProfileRepository
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.user.Profile
import nl.jaysh.models.user.User
import nl.jaysh.models.user.UserResponse
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) {
    fun getAll(): List<User> {
        return userRepository.getAll()
    }

    fun findById(userId: UUID): UserResponse? {
        val user = userRepository.findById(userId = userId) ?: return null
        val profile = profileRepository.findById(userId = userId)

        return UserResponse(
            id = user.id,
            email = user.email,
            profile = profile,
        )
    }

    fun updateUser(user: User): User {
        return userRepository.update(user = user)
    }

    fun deleteUser(userId: UUID) {
        userRepository.delete(userId = userId)
    }

    fun saveProfile(profile: Profile, userId: UUID): Profile {
        return profileRepository.save(profile = profile, userId = userId)
    }

    fun deleteProfile(userId: UUID) {
        profileRepository.delete(userId = userId)
    }
}
