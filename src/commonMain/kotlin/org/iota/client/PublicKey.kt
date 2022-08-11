package org.iota.client

import kotlin.jvm.Synchronized

class PublicKey {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Verify the signature and bytes against this public key
     * @param sig The signature to verify
     * @param bytes The bytes to verify
     */
    fun verify(sig: Signature, bytes: ByteArray): Boolean {
        val a0 = sig.mNativeObj
        sig.mNativeObj = 0
        val ret = do_verify(mNativeObj, a0, bytes)
        //TODO  java.lang.ref.Reference.reachabilityFence(sig)
        return ret
    }

    /**
     * Turns ths public key into bytes
     */
    fun toBytes(): ByteArray {
        return do_toBytes(mNativeObj)
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
        private external fun do_verify(self: Long, sig: Long, bytes: ByteArray): Boolean
        private external fun do_toBytes(self: Long): ByteArray

        /**
         * Attempt to create a public key from the provided bytes
         * @param bytes The bytes to create the key from
         */
        fun tryFromBytes(bytes: ByteArray): PublicKey {
            val ret = do_tryFromBytes(bytes)
            return PublicKey(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_tryFromBytes(bytes: ByteArray): Long
        private external fun do_delete(me: Long)
    }
}