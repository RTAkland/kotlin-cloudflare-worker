plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    explicitApi()

    js(IR) {
        nodejs { binaries.executable() }
    }

    sourceSets {
        jsMain.dependencies {
            api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.1")
            api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.8.1")
        }
    }
}