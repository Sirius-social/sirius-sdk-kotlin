package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Describes a deposit to a single address which is unlocked via a signature.
 */
class SignatureLockedSingleOutputDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Returns the amount of a `SignatureLockedSingleOutputDto`.
     */
    fun amount(): Long {
        return do_amount(mNativeObj)
    }

    /**
     * Returns the address of a `SignatureLockedSingleOutputDto`.
     */
    fun address(): AddressDto {
        val ret = do_address(mNativeObj)
        return AddressDto(InternalPointerMarker.RAW_PTR, ret)
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
        private external fun do_amount(self: Long): Long
        private external fun do_address(self: Long): Long
        private external fun do_delete(me: Long)
    }
}