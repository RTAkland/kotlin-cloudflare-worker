/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 17:29
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.cfworker

import org.w3c.fetch.ResponseInit

/**
 * Auto add cors response headers
 */
@Suppress("FunctionName")
internal fun WorkerApplication._addCorsHeaders(init: ResponseInit, ): ResponseInit {
    val headers = if (this.corsConfig.enabled) {
        val corsHeaders = init.headers ?: js("{}")
        corsHeaders["Access-Control-Allow-Origin"] = corsConfig.origin
        corsHeaders["Access-Control-Allow-Methods"] = corsConfig.allowedMethods.joinToString(", ")
        corsHeaders["Access-Control-Allow-Headers"] = "Content-Type, Authorization"
        if (corsConfig.allowCredentials) corsHeaders["Access-Control-Allow-Credentials"] = corsConfig.allowCredentials
        corsHeaders["Access-Control-Max-Age"] = "${corsConfig.maxAge}"
        corsHeaders
    } else init.headers
    return ResponseInit(
        headers = headers,
        status = init.status,
        statusText = init.statusText
    )
}