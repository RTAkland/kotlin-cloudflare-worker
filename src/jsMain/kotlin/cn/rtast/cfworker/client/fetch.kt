/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.client

import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.Promise

public external fun fetch(
    input: String,
    init: RequestInit = definedExternally,
): Promise<Response>