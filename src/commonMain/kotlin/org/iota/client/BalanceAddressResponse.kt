package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Response of an address balance request
 */
class BalanceAddressResponse {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is BalanceAddressResponse) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: BalanceAddressResponse): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
    //TODO    java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * The type of address
     */
    fun addressType(): Short {
        return do_addressType(mNativeObj)
    }

    /**
     * The address
     */
    fun address(): String {
        return do_address(mNativeObj)
    }

    /**
     * The balance of this address
     */
    fun balance(): Long {
        return do_balance(mNativeObj)
    }

    /**
     * Wether or not this address allows dust
     */
    fun dustAllowed(): Boolean {
        return do_dustAllowed(mNativeObj)
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
        check(marker ==InternalPointerMarker.RAW_PTR)
        mNativeObj = ptr
    }

    /*package*/
    var mNativeObj: Long = 0

    companion object {
        private external fun do_to_string(self: Long): String
        private external fun do_rustEq(self: Long, o: Long): Boolean
        private external fun do_addressType(self: Long): Short
        private external fun do_address(self: Long): String
        private external fun do_balance(self: Long): Long
        private external fun do_dustAllowed(self: Long): Boolean
        private external fun do_delete(me: Long)
    }
}