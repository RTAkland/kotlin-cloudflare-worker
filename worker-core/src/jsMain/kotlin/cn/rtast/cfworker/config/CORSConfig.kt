/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.config

import cn.rtast.cfworker.HttpMethod

public class CORSConfig {
    /**
     * enabled cors agent
     */
    public var enabled: Boolean = true

    /**
     * Allowed origin
     */
    public var origin: String = "*"

    /**
     * Allowed methods
     */
    public var allowedMethods: Set<HttpMethod> = setOf(
        HttpMethod.GET, HttpMethod.POST,
        HttpMethod.DELETE, HttpMethod.DELETE,
        HttpMethod.OPTIONS
    )
}