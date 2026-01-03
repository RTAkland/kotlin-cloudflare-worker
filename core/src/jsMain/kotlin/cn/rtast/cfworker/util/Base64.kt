/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.util

import kotlin.io.encoding.Base64

internal val String.decodeBase64String: String
    get() = Base64.decode(this.encodeToByteArray()).decodeToString()