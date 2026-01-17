/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/17
 */


@file:OptIn(ExperimentalSerializationApi::class)

package cn.rtast.cfworker.serialization.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

public inline fun <reified T> ByteArray.fromProtobuf(): T {
    return ProtoBuf.decodeFromByteArray<T>(this)
}

public inline fun <reified T> T.toProtobuf(): ByteArray {
    return ProtoBuf.encodeToByteArray(this)
}