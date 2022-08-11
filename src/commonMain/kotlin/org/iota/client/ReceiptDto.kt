package org.iota.client

import kotlin.jvm.Synchronized


class ReceiptDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Get the receipt payload
     */
    fun receipt(): ReceiptPayloadDto {
        val ret = do_receipt(mNativeObj)
        return ReceiptPayloadDto(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Get the milestone index this receipt is related to
     */
    fun milestoneIndex(): Long {
        return do_milestoneIndex(mNativeObj)
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
        private external fun do_receipt(self: Long): Long
        private external fun do_milestoneIndex(self: Long): Long
        private external fun do_delete(me: Long)
    }
}