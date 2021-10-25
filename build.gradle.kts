plugins {
    kotlin("jvm") version "1.5.31"
}

group = "dev.cbyrne.kpm"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("script-util"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("scripting-compiler-embeddable"))

    implementation("com.github.ajalt.clikt:clikt:3.3.0")
}