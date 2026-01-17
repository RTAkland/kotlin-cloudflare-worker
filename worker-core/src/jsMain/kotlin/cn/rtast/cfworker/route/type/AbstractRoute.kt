/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:04
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.cfworker.route.type

import cn.rtast.cfworker.HttpMethod
import cn.rtast.cfworker.auth.credentials.HttpCredential
import cn.rtast.cfworker.auth.provider.Authenticator
import cn.rtast.cfworker.route.Handler
import org.w3c.fetch.Request
import org.w3c.fetch.Response


internal interface AbstractRoute {
    /**
     * Route type
     */
    val type: RouteType

    /**
     * Route code handle block
     */
    val block: Handler

    /**
     * Allowed http methods
     */
    val methods: Set<HttpMethod>

    /**
     * Endpoint authenticator, if null, no auth required
     */
    val authenticator: Authenticator<HttpCredential>?

    /**
     * A route handler to process incoming http request
     * @see Handler
     */
    suspend fun handle(request: Request): Response = block.invoke(request)
}