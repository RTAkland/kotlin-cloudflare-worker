/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


/**
 * This file is for internally use
 */

@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)

package cn.rtast.cfworker

import cn.rtast.cfworker.response.respondText
import cn.rtast.cfworker.route.route
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.dom.events.EventListener
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import kotlin.js.Promise

public fun main() {
    @Suppress("unused_expression")
    val eventListener = EventListener { event ->
        val dyn = event.asDynamic()
        event.asDynamic().respondWith(handleRequest(dyn.request as Request))
        Unit
    }
    js("addEventListener('fetch', eventListener)")
}

@JsExport
public fun handleRequest(request: Request): Promise<Response> = GlobalScope.promise {
    val server = WorkerApplication().apply {
        route("/") {
            respondText("Hello kotlin cloudflare worker")
        }
    }
    return@promise server.handle(request)
}
