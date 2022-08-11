package org.iota.client

import kotlin.jvm.Synchronized

/**
 * `TreasuryInput` is an input which references a milestone which generated a `TreasuryOutput`.
 */
class TreasuryInput {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is TreasuryInput) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    /**
     * Create a new TreasuryInput
     * @param id The MilestoneId which Generated a `TreasuryOutput`
     */
    constructor(id: MilestoneId) {
        val a0: Long = id.mNativeObj
        id.mNativeObj = 0
        mNativeObj = init(a0)
        //TODO  java.lang.ref.Reference.reachabilityFence(id)
    }

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: TreasuryInput): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO  java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * Returns the milestones id of a `TreasuryInput`.
     */
    fun milestoneId(): org.iota.client.MilestoneId {
        val ret = do_milestoneId(mNativeObj)
        return MilestoneId(InternalPointerMarker.RAW_PTR, ret)
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
    var mNativeObj: Long

    companion object {
        private external fun init(id: Long): Long
        private external fun do_to_string(self: Long): String
        private external fun do_rustEq(self: Long, o: Long): Boolean
        private external fun do_milestoneId(self: Long): Long
        private external fun do_delete(me: Long)
    }
}