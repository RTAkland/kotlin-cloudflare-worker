/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


@file:OptIn(DelicateCoroutinesApi::class)

package cn.rtast.cfworker.websocket

import cn.rtast.cfworker.util.toByteArray
import cn.rtast.cfworker.websocket.response.respondSwitchingProtocol
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.w3c.dom.CloseEvent
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob

public class WebsocketEventHandler(public val request: Request) {
    private var onMessageBlock: (suspend (MessageEvent) -> Unit)? = null
    private var onCloseBlock: (suspend (CloseEvent) -> Unit?)? = null
    private var onOpenBlock: (suspend (Event) -> Unit)? = null
    private var onErrorBlock: (suspend (Event) -> Unit)? = null
    private var requireUpgradeHeaderBlock: (suspend (Request) -> Response)? =
        { Response("Expected Upgrade: websocket", ResponseInit(426)) }
    public val clients: MutableList<WebSocket> = mutableListOf()

    public fun upgradeHeaderRequired(block: suspend (Request) -> Response) {
        requireUpgradeHeaderBlock = block
    }

    public fun onMessage(block: suspend (MessageEvent) -> Unit) {
        onMessageBlock = block
    }

    public fun onClose(block: suspend (CloseEvent) -> Unit?) {
        onCloseBlock = block
    }

    public fun onOpen(block: suspend (Event) -> Unit) {
        onOpenBlock = block
    }

    public fun onError(block: suspend (Event) -> Unit) {
        onErrorBlock = block
    }

    public fun close(code: Short = 1000, reason: String? = null): Unit =
        this.clients.forEach { it.close(code, reason ?: "Closed") }

    internal suspend fun handle(): Response {
        val upgradeHeader = request.headers.get("upgrade")
        if (upgradeHeader == null || upgradeHeader != "websocket")
            return requireUpgradeHeaderBlock!!.invoke(request)
        val pair = js("new WebSocketPair()")
        val client = pair[0]
        val server = pair[1]
        server.accept()
        clients.add(server)
        server.addEventListener("message", { e: MessageEvent -> GlobalScope.promise { onMessageBlock?.invoke(e) } })
        server.addEventListener("error", { e: Event -> GlobalScope.promise { onErrorBlock?.invoke(e) } })
        server.addEventListener("open", { e: Event -> GlobalScope.promise { onErrorBlock?.invoke(e) } })
        server.addEventListener("close", { e: CloseEvent ->
            GlobalScope.promise {
                onCloseBlock?.invoke(e)
                clients.remove(client)
            }
        })
        return respondSwitchingProtocol(client)
    }
}

public typealias WebsocketHandler = suspend WebsocketEventHandler.() -> Unit

public fun MessageEvent.readText(): String =
    this.data.unsafeCast<String>()

public fun MessageEvent.readByteArray(): ByteArray =
    this.data.unsafeCast<ArrayBuffer>().toByteArray()

public fun MessageEvent.readBlob(): Blob =
    this.data.unsafeCast<Blob>()

public fun MessageEvent.readUint8Array(): Uint8Array =
    this.data.unsafeCast<Uint8Array>()