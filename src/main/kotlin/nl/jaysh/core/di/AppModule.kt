package nl.jaysh.core.di

import nl.jaysh.data.repositories.FoodRepository
import nl.jaysh.services.FoodService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::FoodRepository)
    singleOf(::FoodService)
}
