plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildconfig)
    application
}

group = "dev.gianmarcodavid.telegram"
version = "0.1"

dependencies {
    implementation(project(":commands"))
    implementation(project(":foundation"))
    implementation(project(":foundation-api"))

    implementation(libs.kotlinInject.runtime)
    ksp(libs.kotlinInject.compiler)
    implementation(libs.ktor.server.netty)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.tgbotapi)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application.mainClass = "dev.gianmarcodavid.telegram.MainKt"

val fatJar = tasks.register<Jar>("fatJar") {
    manifest {
        attributes["Main-Class"] = "dev.gianmarcodavid.telegram.MainKt"
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    with(tasks.jar.get() as CopySpec)
}
