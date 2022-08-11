package org.iota.client

import kotlin.jvm.Synchronized


class ReceiptPayloadDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The type of receipt
     */
    fun kind(): Long {
        return do_kind(mNativeObj)
    }

    /**
     * The milestone index at which the funds of a `ReceiptPayload` were migrated
     */
    fun migratedAt(): Long {
        return do_migratedAt(mNativeObj)
    }

    /**
     * The funds which are migrated
     */
    fun funds(): Array<org.iota.client.MigratedFundsEntryDto> {
        return do_funds(mNativeObj)
    }

    /**
     * Whether a `ReceiptPayload` is the final one for a given migrated at index.
     */
    fun last(): Boolean {
        return do_last(mNativeObj)
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
        private external fun do_kind(self: Long): Long
        private external fun do_migratedAt(self: Long): Long
        private external fun do_funds(self: Long): Array<org.iota.client.MigratedFundsEntryDto>
        private external fun do_last(self: Long): Boolean
        private external fun do_delete(me: Long)
    }
}