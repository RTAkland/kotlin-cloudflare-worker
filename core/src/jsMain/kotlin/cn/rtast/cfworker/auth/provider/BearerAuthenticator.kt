/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.auth.provider

import cn.rtast.cfworker.auth.AuthResult
import cn.rtast.cfworker.auth.credentials.BearerCredential
import org.w3c.fetch.Request

/**
 * Bearer token auth provider
 * Schema: Bearer <String>
 */
public fun interface BearerAuthenticator : Authenticator<BearerCredential> {
    override suspend fun authenticate(request: Request, credential: BearerCredential?): AuthResult
}