plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "rao.vishnu"
version = "0.1.0"

dependencies {
    implementation(project(":common"))

    implementation("io.ktor:ktor-client-core:3.1.3")
    implementation("io.ktor:ktor-client-cio:3.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.test {
    useJUnitPlatform() {
        excludeTags("integrationTest")
    }
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests with optional -PbaseUrl"
    group = "verification"

    useJUnitPlatform {
        includeTags("integrationTest")
    }

    testLogging {
        events("PASSED", "SKIPPED", "FAILED")
    }
    // Pass the baseUrl property to tests via system properties
    systemProperty("baseUrl", project.findProperty("baseUrl") ?: "http://localhost:8080")
}

// TODO add publish task