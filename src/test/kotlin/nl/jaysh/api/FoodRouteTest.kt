package nl.jaysh.api

import nl.jaysh.di.appModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class FoodRouteTest {

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(appModule)
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }


}
