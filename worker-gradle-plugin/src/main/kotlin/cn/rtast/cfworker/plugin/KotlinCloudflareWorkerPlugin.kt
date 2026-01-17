/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/17
 */


package cn.rtast.cfworker.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import java.io.File

class KotlinCloudflareWorkerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("wrangler", WorkerExtension::class.java)
        val layout = target.layout
        val wranglerRunDir = layout.buildDirectory.dir("wrangler-run")
        target.tasks.register("prepareWranglerRun", Copy::class.java) {
            it.group = "wrangler"
            it.dependsOn("compileDevelopmentExecutableKotlinJs")
            val buildOutputDir = layout.buildDirectory.dir("compileSync/js/main/developmentExecutable/kotlin")
            it.from(buildOutputDir)
            it.into(wranglerRunDir)
        }
        target.tasks.register("wranglerDev", Exec::class.java) { exec ->
            exec.group = "wrangler"
            exec.workingDir = layout.buildDirectory.dir("wrangler-run").get().asFile.apply { mkdirs() }
            exec.doFirst {
                val sourceDir = target.layout.projectDirectory
                mutableMapOf(extension.wranglerFile.get() to File(exec.workingDir, "wrangler.toml")).apply {
                    val envFile = sourceDir.file(".dev.vars").asFile
                    if (envFile.exists()) this[envFile] = File(exec.workingDir, ".dev.vars")
                }.forEach { (s, d) -> s.copyTo(d, overwrite = true) }
            }
            exec.commandLine(
                if (System.getProperty("os.name").lowercase().contains("windows")) listOf(
                    "cmd", "/c", "wrangler dev --port ${extension.port.get()}"
                )
                else listOf("sh", "-c", "wrangler dev --port ${extension.port.get()}")
            )
            exec.standardInput = System.`in`
            exec.isIgnoreExitValue = false
        }

        val wranglerDeployDir = layout.buildDirectory.dir("wrangler-deploy")
            .apply { get().asFile.deleteRecursively() }
        target.tasks.register("prepareProductionDeploy", Copy::class.java) { copy ->
            copy.group = "wrangler"
            copy.dependsOn("compileProductionExecutableKotlinJs")
            val buildOutputDir = layout.buildDirectory.dir("compileSync/js/main/productionExecutable/kotlin")
            copy.from(buildOutputDir) { it.exclude("*.map") }
            copy.into(wranglerDeployDir)
            copy.from(layout.projectDirectory.file("wrangler.toml"))
            copy.into(wranglerDeployDir)
        }

        target.tasks.register("wranglerDeploy", Exec::class.java) {
            it.group = "wrangler"
            it.dependsOn("prepareProductionDeploy")
            it.workingDir = layout.buildDirectory.dir("wrangler-deploy").get().asFile.apply { mkdirs() }
            it.commandLine(
                if (System.getProperty("os.name").lowercase().contains("windows")) listOf(
                    "cmd",
                    "/c",
                    "wrangler deploy"
                )
                else listOf("sh", "-c", "wrangler deploy")
            )
            it.standardInput = System.`in`
        }
    }
}