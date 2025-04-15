plugins {
    kotlin("jvm") version "1.9.23"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // No external dependencies needed - using only stdlib and Java APIs
}

application {
    mainClass.set("OpenAIDirectKt")
}
