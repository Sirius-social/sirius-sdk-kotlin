package org.iota.client

import kotlin.jvm.Synchronized


class ReferenceUnlock {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Return the index of a `ReferenceUnlock`.
     */
    fun index(): Int {
        return do_index(mNativeObj)
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
         * Creates a new `ReferenceUnlock`.
         * @param index The unlock block index we use for signature
         */
        fun from(index: Int): ReferenceUnlock {
            val ret = do_from(index)
            return ReferenceUnlock(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_from(index: Int): Long
        private external fun do_index(self: Long): Int
        private external fun do_delete(me: Long)
    }
}