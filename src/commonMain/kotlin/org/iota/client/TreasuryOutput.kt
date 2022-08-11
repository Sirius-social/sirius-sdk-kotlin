package org.iota.client

import kotlin.jvm.Synchronized

/**
 * Trasury output.
 */
class TreasuryOutput {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Returns the amount of a `TreasuryOutput`.
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

        /**
         * Creates a new TreasuryOutput with the amount supplied
         * @param amount The amount to set
         */
        fun from(amount: Long): TreasuryOutput {
            val ret = do_from(amount)
            return TreasuryOutput(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_from(amount: Long): Long
        private external fun do_amount(self: Long): Long
        private external fun do_delete(me: Long)
    }
}