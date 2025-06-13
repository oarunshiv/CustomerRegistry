plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "rao.vishnu"
version = "1.0"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
}
