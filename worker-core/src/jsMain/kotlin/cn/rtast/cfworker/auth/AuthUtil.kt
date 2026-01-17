/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.auth

import cn.rtast.cfworker.auth.credentials.BasicCredential
import cn.rtast.cfworker.auth.credentials.BearerCredential

internal fun String.packBasicCredential(): BasicCredential {
    val parts = this.split(":")
    return BasicCredential(parts.first(), parts.last())
}

internal fun String.packBearerCredential(): BearerCredential = BearerCredential(this)