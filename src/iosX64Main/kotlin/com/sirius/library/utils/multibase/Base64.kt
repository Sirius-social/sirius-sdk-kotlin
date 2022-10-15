package com.sirius.library.utils.multibase

import com.ionspin.kotlin.crypto.util.Base64Variants
import com.ionspin.kotlin.crypto.util.LibsodiumUtil
import io.ktor.utils.io.core.*

actual class Base64 {


    actual companion object {
        actual fun getUrlDecoder(): Decoder {
          return Decoder(true)
        }

        actual fun getDecoder(): Decoder {
            return Decoder(false)
        }

        actual fun getUrlEncoder(): Encoder {
            return Encoder(true)
        }

        actual fun getEncoder(): Encoder {
            return Encoder(false)
        }

    }

    actual class Decoder actual constructor(var isUrl: Boolean) {
        actual fun decode(byteArray: ByteArray): ByteArray {
            if(isUrl){
                return LibsodiumUtil.fromBase64(byteArray.decodeToString(), Base64Variants.URLSAFE).toByteArray()
            }
            return LibsodiumUtil.fromBase64(byteArray.decodeToString(), Base64Variants.ORIGINAL).toByteArray()
        }

        actual fun decode(string: String): ByteArray {
            if(isUrl){
                return LibsodiumUtil.fromBase64(string, Base64Variants.URLSAFE).toByteArray()
            }
            return LibsodiumUtil.fromBase64(string, Base64Variants.ORIGINAL).toByteArray()
        }

    }

    actual class Encoder actual constructor(var isUrl: Boolean) {
        actual fun encode(byteArray: ByteArray?): ByteArray {
            if(isUrl){
                return LibsodiumUtil.toBase64(
                    byteArray?.toUByteArray() ?: ByteArray(0).toUByteArray(), Base64Variants.URLSAFE
                ).toByteArray()
            }
            return LibsodiumUtil.toBase64(
                byteArray?.toUByteArray() ?: ByteArray(0).toUByteArray(),
                Base64Variants.ORIGINAL
            ).toByteArray()

        }
    }

}