package org.iota.client

import kotlin.jvm.Synchronized

class UnlockBlocks {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Gets a clone of an `UnlockBlock` from `UnlockBlocks`.
     * Returns the referenced unlock block if the requested unlock block was a reference.
     */
    operator fun get(index: Long): UnlockBlock? {
        val ret = do_get(mNativeObj, index)
        val convRet: UnlockBlock? = if (ret != 0L) {
              UnlockBlock(
                    InternalPointerMarker.RAW_PTR,
                    ret
            )
        } else {
           null
        }
        return convRet
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
        fun from(unlock_blocks: Array<org.iota.client.UnlockBlock>): UnlockBlocks {
            val ret = do_from(unlock_blocks)
            return UnlockBlocks(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_from(unlock_blocks: Array<org.iota.client.UnlockBlock>): Long
        private external fun do_get(self: Long, index: Long): Long
        private external fun do_delete(me: Long)
    }
}