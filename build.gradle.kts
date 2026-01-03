plugins {
    kotlin("multiplatform") version "2.2.21" apply false
    id("maven-publish")
}




allprojects {
    group = "cn.rtast.kotlin-cfworker"
    version = "1.0.2"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
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