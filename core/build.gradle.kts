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


val wranglerRunDir: Provider<Directory> = layout.buildDirectory.dir("wrangler-run")
tasks.register<Copy>("prepareWranglerRun") {
    group = "wrangler"
    dependsOn("compileDevelopmentExecutableKotlinJs")
    val buildOutputDir = layout.buildDirectory.dir("compileSync/js/main/developmentExecutable/kotlin")
    from(buildOutputDir)
    into(wranglerRunDir)
}

val wranglerDev by tasks.registering(Exec::class) {
    group = "wrangler"
    workingDir = layout.buildDirectory.dir("wrangler-run").get().asFile.apply { mkdirs() }
    doFirst {
        val sourceDir = project.layout.projectDirectory
        mapOf(
            sourceDir.file("wrangler.toml").asFile to File(workingDir, "wrangler.toml"),
            sourceDir.file(".dev.vars").asFile to File(workingDir, ".dev.vars"),
        ).forEach { (s, d) -> s.copyTo(d, overwrite = true) }
    }
    commandLine(
        if (System.getProperty("os.name").lowercase().contains("windows")) listOf(
            "cmd", "/c", "wrangler dev --port 7071"
        )
        else listOf("sh", "-c", "wrangler dev --port 7071")
    )
    standardInput = System.`in`
    isIgnoreExitValue = false
}

val wranglerDeployDir: Provider<Directory> = layout.buildDirectory.dir("wrangler-deploy")
    .apply { get().asFile.deleteRecursively() }
val prepareProductionDeploy by tasks.registering(Copy::class) {
    group = "wrangler"
    dependsOn("compileProductionExecutableKotlinJs")
    val buildOutputDir = layout.buildDirectory.dir("compileSync/js/main/productionExecutable/kotlin")
    from(buildOutputDir) { exclude("*.map") }
    into(wranglerDeployDir)
    from(layout.projectDirectory.file("wrangler.toml"))
    into(wranglerDeployDir)
}

val wranglerDeploy by tasks.registering(Exec::class) {
    group = "wrangler"
    dependsOn(prepareProductionDeploy)
    workingDir = layout.buildDirectory.dir("wrangler-deploy").get().asFile.apply { mkdirs() }
    commandLine(
        if (System.getProperty("os.name").lowercase().contains("windows")) listOf("cmd", "/c", "wrangler deploy")
        else listOf("sh", "-c", "wrangler deploy")
    )
    standardInput = System.`in`
}