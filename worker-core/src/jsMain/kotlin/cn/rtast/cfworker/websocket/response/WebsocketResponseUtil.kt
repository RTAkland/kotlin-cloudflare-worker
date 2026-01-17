/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.websocket.response

import org.w3c.fetch.Response

/**
 * Respond switching protocol
 * Websocket
 */
internal fun respondSwitchingProtocol(client: dynamic): Response =
    Response(null, js("{ status: 101, webSocket: client }"))