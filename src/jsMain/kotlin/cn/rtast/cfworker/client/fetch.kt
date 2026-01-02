/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.client

import cn.rtast.cfworker.util.toByteArray
import kotlinx.coroutines.await
import org.w3c.dom.url.URL
import org.w3c.fetch.Request
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.Promise

/**
 * define fetch function externally
 */
public external fun fetch(
    input: String,
    init: RequestInit = definedExternally,
): Promise<Response>

/**
 * Get request raw body, http POST
 */
public suspend fun Request.rawBody(): ByteArray =
    this.arrayBuffer().await().toByteArray()

/**
 * Get [URL] object
 */
public val Request.Url: URL get() = URL(this.url)