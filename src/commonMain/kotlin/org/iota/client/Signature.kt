package org.iota.client

import kotlin.jvm.Synchronized


class Signature {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Turn the signature into bytes
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
           // super.finalize()
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
        private external fun do_toBytes(self: Long): ByteArray

        /**
         * Turns bytes into a signature
         */
        fun fromBytes(bs: ByteArray): Signature {
            val ret = do_fromBytes(bs)
            return Signature(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_fromBytes(bs: ByteArray): Long
        private external fun do_delete(me: Long)
    }
}