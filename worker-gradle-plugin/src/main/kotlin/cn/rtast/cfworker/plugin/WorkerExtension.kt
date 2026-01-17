/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/17
 */


package cn.rtast.cfworker.plugin

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import java.io.File

abstract class WorkerExtension {
    @get:Input
    abstract val port: Property<Int>

    @get:Input
    abstract val wranglerFile: Property<File>

    init {
        wranglerFile.convention(File("wrangler.toml"))
        port.convention(7071)
    }
}