import io.gitlab.arturbosch.detekt.*

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.flyway)
    application
}

group = "nl.jaysh"
version = "0.0.1"

application {
    mainClass.set("nl.jaysh.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

detekt {
    source.from(files(rootProject.rootDir))
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.persistence)
    implementation(libs.bundles.koin)

    implementation(libs.logback)
    implementation(libs.bcrypt)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.h2)
    testImplementation(libs.mockk)
}

tasks {
    fun SourceTask.config() {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    }

    withType<Detekt>().configureEach {
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
    }

    withType<Detekt>().configureEach {
        jvmTarget = "11"
    }

    withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "11"
    }
}
