package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Represents an input referencing an output.
 */
class UtxoInput {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is UtxoInput) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: UtxoInput): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO    java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * Returns the `TransactionId` of the Output.
     */
    fun transactionId(): org.iota.client.TransactionId {
        val ret = do_transactionId(mNativeObj)
        return org.iota.client.TransactionId(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Returns the index of the Output.
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
        private external fun do_rustEq(self: Long, o: Long): Boolean

        /**
         * Create a new `UtxoInput`
         * @param id The ouput Id
         * @param index The output Index
         */
        fun from(id: org.iota.client.TransactionId, index: Int): UtxoInput {
            val a0: Long = id.mNativeObj
            id.mNativeObj = 0
            val ret = do_from(a0, index)
            val convRet = UtxoInput(InternalPointerMarker.RAW_PTR, ret)
            //TODO     java.lang.ref.Reference.reachabilityFence(id)
            return convRet
        }

        private external fun do_from(id: Long, index: Int): Long
        private external fun do_transactionId(self: Long): Long
        private external fun do_index(self: Long): Int
        private external fun do_delete(me: Long)
    }
}