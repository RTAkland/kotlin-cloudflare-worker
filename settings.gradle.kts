@file:Suppress("UnstableApiUsage")

include(":worker-core")
include(":worker-serialization-json")
include(":worker-serialization-protobuf")
include(":worker-gradle-plugin")

pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.maven.rtast.cn/releases")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.maven.rtast.cn/releases")
    }
}

rootProject.name = "kotlin-cloudflare-worker"