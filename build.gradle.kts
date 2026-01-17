plugins {
    kotlin("jvm") version "2.2.21" apply false
    kotlin("multiplatform") version "2.2.21" apply false
    kotlin("plugin.serialization") version "2.2.21" apply false
    id("maven-publish")
}

val libVersion: String by extra

allprojects {
    group = "kotlin-cloudflare-worker"
    version = libVersion

    repositories {
        mavenCentral()
        maven("https://repo.maven.rtast.cn/releases")
    }
}

subprojects {
    apply(plugin = "maven-publish")

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
}