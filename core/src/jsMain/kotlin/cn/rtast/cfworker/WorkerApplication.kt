/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:03
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.cfworker

import cn.rtast.cfworker.auth.AuthResult
import cn.rtast.cfworker.auth.packBasicCredential
import cn.rtast.cfworker.auth.packBearerCredential
import cn.rtast.cfworker.auth.provider.BasicAuthenticator
import cn.rtast.cfworker.auth.provider.BearerAuthenticator
import cn.rtast.cfworker.config.CORSConfig
import cn.rtast.cfworker.response.respondEmpty
import cn.rtast.cfworker.response.respondText
import cn.rtast.cfworker.route.type.AbstractRoute
import cn.rtast.cfworker.route.type.RegexRoute
import cn.rtast.cfworker.route.type.StringRoute
import cn.rtast.cfworker.util.decodeBase64String
import cn.rtast.cfworker.websocket.WebsocketEventHandler
import cn.rtast.cfworker.websocket.WebsocketRoute
import org.w3c.dom.url.URL
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit

/**
 * Kotlin cloudflare worker logic entrypoint class
 */
public class WorkerApplication(
    public val corsConfig: CORSConfig = CORSConfig(),
) {
    internal val routes: MutableList<AbstractRoute> = mutableListOf()
    internal val websocketRoutes: MutableList<WebsocketRoute> = mutableListOf()

    public suspend fun handle(request: Request): Response {
        val url = URL(request.url)
        val path = url.pathname
        val method = HttpMethod.fromString(request.method) ?: HttpMethod.GET
        if (method == HttpMethod.OPTIONS && corsConfig.enabled) return respondEmpty()
        if (request.headers.get("upgrade") != null) {
            val route = websocketRoutes.firstOrNull { r ->
                r.stringPath?.let { it == url.pathname } ?: r.regexPath?.matches(url.pathname) ?: false
            } ?: return Response("Not Found", ResponseInit(404))
            val handler = WebsocketEventHandler(request = request)
            route.block(handler)
            return handler.handle()
        }
        val matchedRoutes = routes.filter {
            when (it) {
                is StringRoute -> it.path == path
                is RegexRoute -> it.path.matches(path)
                else -> false
            }
        }

        if (matchedRoutes.isEmpty()) return respondText("Not Found", 404)
        val methodMatchedRoute = matchedRoutes.firstOrNull { method in it.methods }
        if (methodMatchedRoute == null) {
            val allowMethods = matchedRoutes.flatMap { it.methods }.distinct().joinToString(", ")
            val headers: dynamic = js("{}")
            headers["Allow"] = allowMethods
            return respondText("Method Not Allowed", 405)
        }

        val auth = methodMatchedRoute.authenticator
        val responsePromise = if (auth != null) {
            val credential = when (auth) {
                is BasicAuthenticator -> request.headers.get("authorization")
                    ?.removePrefix("Basic ")
                    ?.decodeBase64String
                    ?.packBasicCredential()

                is BearerAuthenticator -> request.headers.get("authorization")
                    ?.removePrefix("Bearer ")
                    ?.packBearerCredential()
            }
            when (auth.authenticate(request, credential)) {
                AuthResult.OK -> methodMatchedRoute.handle(request)
                AuthResult.UNAUTHORIZED -> respondText("UNAUTHORIZED", 401)
                AuthResult.FORBIDDEN -> respondText("FORBIDDEN", 403)
            }
        } else methodMatchedRoute.handle(request)
        return responsePromise
    }
}