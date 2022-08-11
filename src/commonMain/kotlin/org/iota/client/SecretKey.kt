package org.iota.client

import kotlin.jvm.Synchronized


class SecretKey {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Derive a public key from the secret key
     */
    fun publicKey(): org.iota.client.PublicKey {
        val ret = do_publicKey(mNativeObj)
        return org.iota.client.PublicKey(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Turn this Secret key into bytes
     */
    fun toBytes(): ByteArray {
        return do_toBytes(mNativeObj)
    }

    /**
     * Sign the bytes using this key
     * @param bytes the Bytes to sign
     */
    fun sign(bytes: ByteArray): Signature {
        val ret = do_sign(mNativeObj, bytes)
        return Signature(InternalPointerMarker.RAW_PTR, ret)
    }

    @Synchronized
    fun delete() {
        if (mNativeObj != 0L) {
            do_delete(mNativeObj)
            mNativeObj = 0
        }
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        try {
            delete()
        } finally {
          //  super.finalize()
        }
    }

    /*package*/
    internal constructor(marker: InternalPointerMarker, ptr: Long) {
        check(marker === InternalPointerMarker.RAW_PTR)
        mNativeObj = ptr
    }

    /*package*/
    var mNativeObj: Long = 0

    companion object {
        private external fun do_to_string(self: Long): String

        /**
         * Generate a new secret key
         */
        fun generate(): SecretKey {
            val ret = do_generate()
            return SecretKey(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_generate(): Long

        /**
         * Create a new secret key from the bytes
         * @param bytes The bytes to create the key from
         */
        fun fromBytes(bytes: ByteArray): SecretKey {
            val ret = do_fromBytes(bytes)
            return SecretKey(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_fromBytes(bytes: ByteArray): Long
        private external fun do_publicKey(self: Long): Long
        private external fun do_toBytes(self: Long): ByteArray
        private external fun do_sign(self: Long, bytes: ByteArray): Long
        private external fun do_delete(me: Long)
    }
}