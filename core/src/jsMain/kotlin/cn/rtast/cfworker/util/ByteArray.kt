/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.cfworker.util

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array

/**
 * Convert [ArrayBuffer] to [ByteArray]
 */
public fun ArrayBuffer.toByteArray(): ByteArray =
    Int8Array(this).unsafeCast<ByteArray>()

/**
 * Convert [ByteArray] to [ArrayBuffer]
 */
public fun ByteArray.toArrayBuffer(): ArrayBuffer =
    Int8Array(this.toTypedArray()).unsafeCast<ArrayBuffer>()