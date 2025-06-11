import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "1.9.10"
    application
}

group = "rao.vishnu"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // Ktor core
    implementation("io.ktor:ktor-server-core-jvm:3.1.3")
    implementation("io.ktor:ktor-server-netty-jvm:3.1.3")

    // JSON support
    implementation("io.ktor:ktor-server-content-negotiation:3.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.5.18")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    testImplementation("io.ktor:ktor-server-test-host:3.1.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("rao.vishnu.customerservice.AppKt")
}
