/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:02
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.cfworker

import org.w3c.fetch.Request

/**
 * Common http method
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Methods
 */
public enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT;

    public companion object {
        public fun fromString(method: String): HttpMethod? =
            entries.firstOrNull { it.name.equals(method, ignoreCase = true) }
    }
}

/**
 * Get parsed http method, if it's not a standard http method return GET
 */
public val Request.httpMethod: HttpMethod
    get() = HttpMethod.fromString(this.method) ?: HttpMethod.GET