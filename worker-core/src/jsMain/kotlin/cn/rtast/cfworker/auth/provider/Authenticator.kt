package cn.rtast.cfworker.auth.provider

import cn.rtast.cfworker.auth.AuthResult
import cn.rtast.cfworker.auth.credentials.HttpCredential
import org.w3c.fetch.Request

/**
 * An interface for authenticating
 * It's sealed
 */
public sealed interface Authenticator<out C : HttpCredential> {
    public suspend fun authenticate(request: Request, credential: @UnsafeVariance C?): AuthResult
}