/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:04
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.cfworker.route.type

import cn.rtast.cfworker.HttpMethod
import cn.rtast.cfworker.auth.credentials.HttpCredential
import cn.rtast.cfworker.auth.provider.Authenticator
import cn.rtast.cfworker.route.Handler

internal data class StringRoute(
    val path: String,
    override val block: Handler,
    override val methods: Set<HttpMethod>,
    override val type: RouteType = RouteType.String,
    override val authenticator: Authenticator<HttpCredential>? = null,
) : AbstractRoute