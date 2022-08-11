package org.iota.client

import kotlin.jvm.Synchronized

/**
 * Output type for deposits that enables an address to receive dust outputs. It can be consumed as an input like a
 * regular SigLockedSingleOutput.
 */
class SignatureLockedDustAllowanceOutput {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Returns the amount of a `SignatureLockedDustAllowanceOutput`.
     */
    fun amount(): Long {
        return do_amount(mNativeObj)
    }

    /**
     * Returns the address of a `SignatureLockedDustAllowanceOutput`.
     */
    fun address(): Address {
        val ret = do_address(mNativeObj)
        return Address(InternalPointerMarker.RAW_PTR, ret)
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

        /**
         * Creates a new `SignatureLockedDustAllowanceOutput`.
         * @param address The address to set
         * @param amount The amount to set
         */
        fun from(address: Address, amount: Long): SignatureLockedDustAllowanceOutput {
            val a0 = address.mNativeObj
            address.mNativeObj = 0
            val ret = do_from(a0, amount)
            val convRet = SignatureLockedDustAllowanceOutput(InternalPointerMarker.RAW_PTR, ret)
            //TODO  java.lang.ref.Reference.reachabilityFence(address)
            return convRet
        }

        private external fun do_from(address: Long, amount: Long): Long
        private external fun do_amount(self: Long): Long
        private external fun do_address(self: Long): Long
        private external fun do_delete(me: Long)
    }
}