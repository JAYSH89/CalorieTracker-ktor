package nl.jaysh.core.di

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.data.repositories.UserRepository
import nl.jaysh.services.FoodService
import nl.jaysh.services.UserService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::FoodRepository)
    singleOf(::FoodService)
    singleOf(::UserRepository)
    singleOf(::UserService)

}
