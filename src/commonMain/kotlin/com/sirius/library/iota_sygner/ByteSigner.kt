package com.sirius.library.iota_sygner

expect class ByteSigner {
}

abstract class ByteSigner protected constructor(val algorithm: String) {

    @Throws(Exception::class)
    fun sign(content: ByteArray?, algorithm: String): ByteArray {
        return if (algorithm != this.algorithm) {
            throw Exception("Unexpected algorithm " + algorithm + " is different from " + this.algorithm)
        } else {
            this.sign(content)
        }
    }

    @Throws(Exception::class)
    protected abstract fun sign(var1: ByteArray?): ByteArray

}
