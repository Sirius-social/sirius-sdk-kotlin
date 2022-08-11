package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Node struct
 */
class Node {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is Node) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: Node): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO   java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * node url
     */
    fun url(): String {
        return do_url(mNativeObj)
    }

    /**
     * node jwt
     */
    fun jwt(): String?{
        val ret = do_jwt(mNativeObj)
        return ret
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
        private external fun do_rustEq(self: Long, o: Long): Boolean
        private external fun do_url(self: Long): String
        private external fun do_jwt(self: Long): String?
        private external fun do_delete(me: Long)
    }
}