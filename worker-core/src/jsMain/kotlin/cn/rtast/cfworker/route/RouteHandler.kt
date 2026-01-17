/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/25 11:43
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.cfworker.route

import org.w3c.fetch.Request
import org.w3c.fetch.Response

public typealias Handler = suspend (Request) -> Response