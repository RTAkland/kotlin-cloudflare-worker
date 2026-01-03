/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:17
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */

@file:Suppress("unused")

package cn.rtast.cfworker.response

import cn.rtast.cfworker.WorkerApplication
import cn.rtast.cfworker._addCorsHeaders
import org.khronos.webgl.Uint8Array
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit

public fun WorkerApplication.respondBytes(
    bytes: ByteArray,
    status: Int = 200,
    contentType: String? = null,
    headers: Map<String, String> = mapOf(),
): Response {
    val respHeaders: dynamic = js("{}")
    headers.forEach { respHeaders[it.key] = it.value }
    respHeaders["Content-Type"] = contentType ?: "application/octet-stream"
    return Response(
        Uint8Array(bytes.toTypedArray()),
        _addCorsHeaders(
            ResponseInit(
                status = status.toShort(),
                headers = respHeaders
            )
        )
    )
}

public fun WorkerApplication.respondStream(
    stream: dynamic,
    status: Int = 200,
    headers: Map<String, String> = mapOf(),
): Response {
    val respHeaders: dynamic = js("{}")
    headers.forEach { respHeaders[it.key] = it.value }
    return Response(
        stream,
        _addCorsHeaders(
            ResponseInit(
                status = status.toShort(),
                headers = respHeaders
            )
        )
    )
}

public fun WorkerApplication.respondEmpty(
    status: Int = 204,
    headers: Map<String, String> = mapOf(),
): Response {
    val respHeaders: dynamic = js("{}")
    headers.forEach { respHeaders[it.key] = it.value }
    return Response(null, _addCorsHeaders(ResponseInit(status = status.toShort(), headers = respHeaders)))
}

public fun WorkerApplication.respondText(
    content: String,
    status: Int = 200,
    headers: Map<String, String> = mapOf(),
): Response {
    val respHeaders: dynamic = js("{}")
    headers.forEach { respHeaders[it.key] = it.value }
    return Response(content, _addCorsHeaders(ResponseInit(status = status.toShort(), headers = respHeaders)))
}

public fun WorkerApplication.respondRedirect(
    location: String,
    status: Int = 302,
    headers: Map<String, String> = mapOf(),
): Response {
    val respHeaders: dynamic = js("{}")
    headers.forEach { respHeaders[it.key] = it.value }
    respHeaders["Location"] = location
    return Response(
        "Redirecting to $location",
        _addCorsHeaders(ResponseInit(status = status.toShort(), headers = respHeaders))
    )
}