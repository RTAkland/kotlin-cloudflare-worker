package cn.rtast.cfworker.auth.provider

import cn.rtast.cfworker.auth.AuthResult
import cn.rtast.cfworker.auth.credentials.BasicCredential
import org.w3c.fetch.Request

/**
 * Basic auth provider
 * Schema: Basic <Base64-encoded username:password string>
 */
public fun interface BasicAuthenticator : Authenticator<BasicCredential> {
    override suspend fun authenticate(request: Request, credential: BasicCredential?): AuthResult
}