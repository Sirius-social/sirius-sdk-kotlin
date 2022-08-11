package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Wrapper for different output types
 */
class OutputDto {
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

    fun asSignatureLockedSingleOutputDto(): SignatureLockedSingleOutputDto {
        val ret = do_asSignatureLockedSingleOutputDto(mNativeObj)
        return SignatureLockedSingleOutputDto(InternalPointerMarker.RAW_PTR, ret)
    }

    fun asSignatureLockedDustAllowanceOutputDto(): SignatureLockedDustAllowanceOutputDto {
        val ret =
            do_asSignatureLockedDustAllowanceOutputDto(mNativeObj)
        return SignatureLockedDustAllowanceOutputDto(InternalPointerMarker.RAW_PTR, ret)
    }

    fun asTreasuryOutput(): TreasuryOutputDto {
        val ret = do_asTreasuryOutput(mNativeObj)
        return TreasuryOutputDto(InternalPointerMarker.RAW_PTR, ret)
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
        private external fun do_kind(self: Long): Int
        private external fun do_asSignatureLockedSingleOutputDto(self: Long): Long
        private external fun do_asSignatureLockedDustAllowanceOutputDto(self: Long): Long
        private external fun do_asTreasuryOutput(self: Long): Long
        private external fun do_delete(me: Long)
    }
}