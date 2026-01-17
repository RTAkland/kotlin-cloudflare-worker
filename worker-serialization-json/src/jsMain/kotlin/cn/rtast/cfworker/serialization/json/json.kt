/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/17
 */


@file:OptIn(ExperimentalSerializationApi::class)

package cn.rtast.cfworker.serialization.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

public val json: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    classDiscriminator = "_json_type_"
    encodeDefaults = true
    coerceInputValues = true
    decodeEnumsCaseInsensitive = true
    isLenient = true
}

public inline fun <reified T> T.toJson(): String {
    return json.encodeToString(this)
}

public inline fun <reified T> String.fromJson(): T {
    return json.decodeFromString<T>(this)
}