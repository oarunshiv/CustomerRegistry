plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "rao.vishnu"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("rao.vishnu.customerservice.AppKt")
}
