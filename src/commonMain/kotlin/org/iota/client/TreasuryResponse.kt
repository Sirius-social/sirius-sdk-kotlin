package org.iota.client

import kotlin.jvm.Synchronized

/**
 * Response of GET /api/v1/treasury.
 * Returns all information about the treasury.
 */
class TreasuryResponse {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is TreasuryResponse) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: TreasuryResponse): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO    java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * The amount in the treasury
     */
    fun amount(): Long {
        return do_amount(mNativeObj)
    }

    /**
     * The milestone ID this reponse relates to
     */
    fun milestoneId(): String {
        return do_milestoneId(mNativeObj)
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
     //       super.finalize()
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
        private external fun do_amount(self: Long): Long
        private external fun do_milestoneId(self: Long): String
        private external fun do_delete(me: Long)
    }
}