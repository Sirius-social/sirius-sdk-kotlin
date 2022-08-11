package org.iota.client

object RustHex {
    external fun encode(a0: String?): String?
    external fun encode(a0: ByteArray?): String?
    external fun decode(s: String?): ByteArray?
    external fun decode(s: ByteArray?): ByteArray?
}