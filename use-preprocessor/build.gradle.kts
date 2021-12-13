plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.6.0-1.0.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    ksp(project(":build-preprocessor"))
}