package org.iota.client

import kotlin.jvm.Synchronized

class MigratedFundsEntryDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The tail transaction hash
     */
    fun tailTransactionHash(): String {
        return do_tailTransactionHash(mNativeObj)
    }

    /**
     * The address this was deposited to
     */
    fun address(): AddressDto {
        val ret = do_address(mNativeObj)
        return AddressDto(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * The amount that was deposited
     */
    fun deposit(): Long {
        return do_deposit(mNativeObj)
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
         //   super.finalize()
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
        private external fun do_tailTransactionHash(self: Long): String
        private external fun do_address(self: Long): Long
        private external fun do_deposit(self: Long): Long
        private external fun do_delete(me: Long)
    }
}