import java.util.*

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildconfig)
}

group = "dev.gianmarcodavid.telegram"

dependencies {
    implementation(libs.kotlinInject.runtime)
}

buildConfig {
    useKotlinOutput { internalVisibility = false }
    Properties()
        .apply { project.parent!!.file("env.properties").inputStream().use(::load) }
        .forEach { key, value -> buildConfigField(key as String, value.toString()) }
}
