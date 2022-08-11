package org.iota.client

import kotlin.jvm.Synchronized


class MigratedFundsEntry {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Returns the tail transaction hash of a `MigratedFundsEntry`.
     */
    fun tailTransactionHash(): String {
        return do_tailTransactionHash(mNativeObj)
    }

    /**
     * Returns the output of a `MigratedFundsEntry`.
     */
    fun output(): SignatureLockedSingleOutput {
        val ret = do_output(mNativeObj)
        return SignatureLockedSingleOutput(InternalPointerMarker.RAW_PTR, ret)
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

        /**
         * Creates a new `MigratedFundsEntry`.
         * @param hash The hash from the transaction used to receive the funds
         * @param output the output related to this transaction
         */
        fun from(hash: String, output: SignatureLockedSingleOutput): MigratedFundsEntry {
            val a1 = output.mNativeObj
            output.mNativeObj = 0
            val ret = do_from(hash, a1)
            val convRet = MigratedFundsEntry(InternalPointerMarker.RAW_PTR, ret)
            //TODO  java.lang.ref.Reference.reachabilityFence(output)
            return convRet
        }

        private external fun do_from(hash: String, output: Long): Long
        private external fun do_tailTransactionHash(self: Long): String
        private external fun do_output(self: Long): Long
        private external fun do_delete(me: Long)
    }
}