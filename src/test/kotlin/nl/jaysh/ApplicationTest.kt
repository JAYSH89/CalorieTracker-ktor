package nl.jaysh

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import nl.jaysh.core.di.appModule
import nl.jaysh.models.AmountType
import nl.jaysh.models.Food
import nl.jaysh.plugins.configureRouting
import nl.jaysh.plugins.configureSerialization
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.*

class ApplicationTest : KoinTest
