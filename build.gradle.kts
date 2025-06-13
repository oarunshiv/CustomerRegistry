plugins {
    kotlin("jvm") version "2.1.20" apply false
    kotlin("plugin.serialization") version "1.9.10" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    afterEvaluate {
        // Only configure if the kotlin extension is present
        extensions.findByName("kotlin")?.let {
            (it as org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension).jvmToolchain(17)
        }
    }
}