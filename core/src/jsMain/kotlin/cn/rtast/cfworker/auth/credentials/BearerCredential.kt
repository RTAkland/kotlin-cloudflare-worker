/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.auth.credentials

/**
 * Bearer credential
 */
public data class BearerCredential(
    val token: String,
) : HttpCredential