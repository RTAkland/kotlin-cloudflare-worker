/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/25 10:49
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */

@file:Suppress("unused")

package cn.rtast.cfworker.route

import cn.rtast.cfworker.HttpMethod
import cn.rtast.cfworker.WorkerApplication
import cn.rtast.cfworker.auth.credentials.HttpCredential
import cn.rtast.cfworker.auth.provider.Authenticator
import cn.rtast.cfworker.route.type.RegexRoute
import cn.rtast.cfworker.route.type.RouteType
import cn.rtast.cfworker.route.type.StringRoute

public fun WorkerApplication.route(
    path: Regex,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    block: Handler,
): Unit = run { routes.add(RegexRoute(path, block, methods)) }

public fun WorkerApplication.route(
    path: String,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    block: Handler,
): Unit = run { routes.add(StringRoute(path, block, methods)) }

public fun WorkerApplication.route(
    path: String,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    authenticator: Authenticator<HttpCredential>,
    block: Handler,
): Unit = run { routes.add(StringRoute(path, block, methods, type = RouteType.String, authenticator = authenticator)) }

public fun WorkerApplication.route(
    path: Regex,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    authenticator: Authenticator<HttpCredential>,
    block: Handler,
): Unit = run { routes.add(RegexRoute(path, block, methods, type = RouteType.Regex, authenticator = authenticator)) }