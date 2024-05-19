package nl.jaysh.core.di

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.models.authentication.JwtConfig
import nl.jaysh.services.FoodService
import nl.jaysh.services.JwtService
import nl.jaysh.services.UserService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun appModule(jwtConfig: JwtConfig) = module {
    single { jwtConfig }

    singleOf(::UserRepository)
    singleOf(::FoodRepository)

    singleOf(::JwtService)
    singleOf(::UserService)
    singleOf(::FoodService)
}
