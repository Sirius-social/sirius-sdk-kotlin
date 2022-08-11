package org.iota.client

import kotlin.jvm.Synchronized


class AddressStringPublicWrapper {
    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is AddressStringPublicWrapper) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    override fun toString(): String {
        run { return this.to_string() }
    }

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private external fun do_to_string(self: Long): String

    private fun rustEq(o: AddressStringPublicWrapper): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
      //TODO   java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    private external fun do_rustEq(self: Long, o: Long): Boolean

    /**
     * If this is a public address
     */
    val isPublic: Boolean
        get() = do_isPublic(mNativeObj)

    private external fun do_isPublic(self: Long): Boolean

    /**
     * The address itself
     */
    fun address(): String {
        return do_address(mNativeObj)
    }

    private external fun do_address(self: Long): String

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
            //super.finalize()
        }
    }

    private external fun do_delete(me: Long)

    /*package*/
    internal  constructor(marker: InternalPointerMarker, ptr: Long) {
        check(marker == InternalPointerMarker.RAW_PTR)
        mNativeObj = ptr
    }

    constructor(){}

    /*package*/
    var mNativeObj: Long = 0
}