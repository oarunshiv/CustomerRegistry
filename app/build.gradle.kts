plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "1.9.10"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    implementation("io.ktor:ktor-server-request-validation:3.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    // ORM framework for Kotlin
    implementation("org.jetbrains.exposed:exposed-core:1.0.0-beta-2")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0-beta-2")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0-beta-2")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.0.0-beta-2")

    implementation("org.postgresql:postgresql:42.7.3")
    testImplementation("com.h2database:h2:2.1.214")
    // Logging
    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.5.18")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    testImplementation("io.ktor:ktor-server-test-host:3.1.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    testLogging {
        events("PASSED", "SKIPPED", "FAILED")
    }
}

application {
    mainClass.set("rao.vishnu.customerservice.AppKt")
}
