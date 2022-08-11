package org.iota.client

import kotlin.jvm.Synchronized


class TreasuryOutputDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The type of Treasury DTO
     */
    fun kind(): Short {
        return do_kind(mNativeObj)
    }

    /**
     * The amount in the treasury
     */
    fun amount(): Long {
        return do_amount(mNativeObj)
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
       //     super.finalize()
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
        private external fun do_kind(self: Long): Short
        private external fun do_amount(self: Long): Long
        private external fun do_delete(me: Long)
    }
}