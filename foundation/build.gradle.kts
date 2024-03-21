plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

group = "dev.gianmarcodavid.telegram"

dependencies {
    implementation(project(":foundation-api"))

    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlinInject.runtime)
    ksp(libs.kotlinInject.compiler)
    implementation(libs.logger.logback.classic)
    implementation(libs.logger.logback.core)
    implementation(libs.logger.slf4j)
    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.serialization)
}
