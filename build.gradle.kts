plugins {
    kotlin("multiplatform") version "2.2.21"
    id("maven-publish")
}

group = "cn.rtast.kotlin-cfworker"
version = "1.0.2"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    js(IR) { nodejs { binaries.executable() } }

    sourceSets {
        jsMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

publishing {
    repositories {
        maven("https://repo.maven.rtast.cn/releases") {
            credentials {
                username = "RTAkland"
                password = System.getenv("PUBLISH_TOKEN")
            }
        }
    }
}