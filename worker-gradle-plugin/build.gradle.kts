plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
}

gradlePlugin {
    website = "https://github.com/RTAkland/kotlin-cloudflare-worker"
    vcsUrl = "https://github.com/RTAkland/kotlin-cloudflare-worker.git"
    plugins {
        create("kotlin-cloudflare-worker") {
            id = "kotlin-cloudflare-worker"
            displayName = "kotlin cloudflare worker"
            description = "Kotlin cloudflare worker gradle plugin"
            tags = listOf("tool", "tasks")
            implementationClass = "cn.rtast.cfworker.plugin.KotlinCloudflareWorkerPlugin"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.21")
}