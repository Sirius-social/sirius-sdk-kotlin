package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Wrapper for node information
 */
class NodeInfoWrapper {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is NodeInfoWrapper) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: NodeInfoWrapper): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO   java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * Get the URL from which this info is derived
     */
    fun url(): String {
        return do_url(mNativeObj)
    }

    /**
     * Get the information
     */
    fun nodeInfo(): org.iota.client.InfoResponse {
        val ret = do_nodeInfo(mNativeObj)
        return org.iota.client.InfoResponse(InternalPointerMarker.RAW_PTR, ret)
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
        //    super.finalize()
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
        private external fun do_url(self: Long): String
        private external fun do_nodeInfo(self: Long): Long
        private external fun do_delete(me: Long)
    }
}