plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
}

group = "dev.gianmarcodavid.telegram"

dependencies {
    implementation(project(":foundation-api"))

    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlinInject.runtime)
    ksp(libs.kotlinInject.compiler)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.sqldelight.driver)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
}

sqldelight {
    databases{
        create("Database"){
            packageName.set("dev.gianmarcodavid.telegram.database")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
