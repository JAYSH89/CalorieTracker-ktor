import io.gitlab.arturbosch.detekt.*

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
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
    implementation(libs.logback)
    implementation(libs.kotlinx.serialization)
    implementation(libs.bundles.koin)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)
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
