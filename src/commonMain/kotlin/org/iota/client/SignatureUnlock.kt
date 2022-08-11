package org.iota.client

import kotlin.jvm.Synchronized


class SignatureUnlock {
    override fun toString(): String {
        run { return this.to_string() }
    }

    /**
     * Create a new Signature inlock block
     * @param public_key The public ket used for this signature block
     * @param signature The signature created for this unlock block
     */
    constructor(public_key: ByteArray, signature: ByteArray) {
        mNativeObj = init(public_key, signature)
    }

    private fun to_string(): String {
        return do_to_string(mNativeObj)
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
    var mNativeObj: Long

    companion object {
        private external fun init(public_key: ByteArray, signature: ByteArray): Long
        private external fun do_to_string(self: Long): String
        private external fun do_delete(me: Long)
    }
}