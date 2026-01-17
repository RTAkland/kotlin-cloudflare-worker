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
internal fun WorkerApplication._addCorsHeaders(
    init: ResponseInit,
    origin: String = "*",
    allowedMethods: Set<HttpMethod> = setOf(
        HttpMethod.GET, HttpMethod.POST,
        HttpMethod.DELETE, HttpMethod.DELETE,
        HttpMethod.OPTIONS
    ),
): ResponseInit {
    val headers = if (this.corsConfig.enabled) {
        val corsHeaders = init.headers ?: js("{}")
        corsHeaders["Access-Control-Allow-Origin"] = origin
        corsHeaders["Access-Control-Allow-Methods"] = allowedMethods.joinToString(", ")
        corsHeaders["Access-Control-Allow-Headers"] = "Content-Type, Authorization"
        corsHeaders["Access-Control-Max-Age"] = "86400"
        corsHeaders
    } else init.headers
    return ResponseInit(
        headers = headers,
        status = init.status,
        statusText = init.statusText
    )
}