package org.iota.client

import kotlin.jvm.Synchronized


class TransactionPayloadBuilder {
    /**
     * Creates a new `TransactionPayloadBuilder`.
     */
    constructor() {
        mNativeObj = init()
    }

    /**
     * Adds an essence to a `TransactionPayloadBuilder`.
     */
    fun withEssence(essence: org.iota.client.Essence): TransactionPayloadBuilder {
        val a0: Long = essence.mNativeObj
        essence.mNativeObj = 0
        val ret = do_withEssence(mNativeObj, a0)
        val convRet = TransactionPayloadBuilder(InternalPointerMarker.RAW_PTR, ret)
        //TODO   java.lang.ref.Reference.reachabilityFence(essence)
        return convRet
    }

    /**
     * Adds unlock blocks to a `TransactionPayloadBuilder`.
     */
    fun withUnlockBlocks(unlock_blocks: UnlockBlocks): TransactionPayloadBuilder {
        val a0 = unlock_blocks.mNativeObj
        unlock_blocks.mNativeObj = 0
        val ret = do_withUnlockBlocks(mNativeObj, a0)
        val convRet = TransactionPayloadBuilder(InternalPointerMarker.RAW_PTR, ret)
        //TODO   java.lang.ref.Reference.reachabilityFence(unlock_blocks)
        return convRet
    }

    /**
     * Finishes a `TransactionPayloadBuilder` into a `TransactionPayload`.
     */
    fun finish(): org.iota.client.TransactionPayload {
        val ret = do_finish(mNativeObj)
        return org.iota.client.TransactionPayload(InternalPointerMarker.RAW_PTR, ret)
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
    var mNativeObj: Long

    companion object {
        private external fun init(): Long
        private external fun do_withEssence(self: Long, essence: Long): Long
        private external fun do_withUnlockBlocks(self: Long, unlock_blocks: Long): Long
        private external fun do_finish(self: Long): Long
        private external fun do_delete(me: Long)
    }
}