package org.iota.client

import kotlin.jvm.Synchronized

class Output {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    fun kind(): OutputKind {
        val ret = do_kind(mNativeObj)
        return OutputKind.fromInt(ret)
    }

    fun asSignatureLockedSingleOutput(): SignatureLockedSingleOutput {
        val ret = do_asSignatureLockedSingleOutput(mNativeObj)
        return SignatureLockedSingleOutput(InternalPointerMarker.RAW_PTR, ret)
    }

    fun asSignatureLockedDustAllowanceOutput(): SignatureLockedDustAllowanceOutput {
        val ret =
            do_asSignatureLockedDustAllowanceOutput(mNativeObj)
        return SignatureLockedDustAllowanceOutput(InternalPointerMarker.RAW_PTR, ret)
    }

    fun asTreasuryOutput(): TreasuryOutput {
        val ret = do_asTreasuryOutput(mNativeObj)
        return TreasuryOutput(InternalPointerMarker.RAW_PTR, ret)
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
        private external fun do_kind(self: Long): Int
        private external fun do_asSignatureLockedSingleOutput(self: Long): Long
        private external fun do_asSignatureLockedDustAllowanceOutput(self: Long): Long
        private external fun do_asTreasuryOutput(self: Long): Long
        private external fun do_delete(me: Long)
    }
}