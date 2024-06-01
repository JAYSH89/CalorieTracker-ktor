package nl.jaysh.core.di

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.data.repositories.JournalRepository
import nl.jaysh.data.repositories.TokenRepository
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.authentication.JwtConfig
import nl.jaysh.services.AuthService
import nl.jaysh.services.FoodService
import nl.jaysh.services.JournalService
import nl.jaysh.services.JwtService
import nl.jaysh.services.UserService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun appModule(jwtConfig: JwtConfig) = module {
    single { jwtConfig }

    singleOf(::TokenRepository)
    singleOf(::UserRepository)
    singleOf(::FoodRepository)
    singleOf(::JournalRepository)

    singleOf(::JwtService)
    singleOf(::AuthService)
    singleOf(::UserService)
    singleOf(::FoodService)
    singleOf(::JournalService)
}
