plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

group = "dev.gianmarcodavid.telegram"

dependencies {
    implementation(project(":foundation-api"))

    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlinInject.runtime)
    ksp(libs.kotlinInject.compiler)
    implementation(libs.okhttp)
    implementation(libs.retrofit)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
}

tasks.test {
    useJUnitPlatform()
}
