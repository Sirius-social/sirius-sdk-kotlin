package org.iota.client

import kotlin.jvm.Synchronized

class Address {
    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is Address) equal = obj.rustEq(this)
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

    private fun rustEq(o: Address): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    private external fun do_rustEq(self: Long, o: Long): Boolean

    /**
     * Tries to create an `Address` from a Bech32 encoded string.
     */
    fun tryFromBech32(addr: String): Address? {
        val ret = do_tryFromBech32(addr)
        return Address(InternalPointerMarker.RAW_PTR, ret)
    }

    private external fun do_tryFromBech32(addr: String): Long

    fun toBech32(hrp: String): String {
        return do_toBech32(mNativeObj, hrp)
    }

    private external fun do_toBech32(self: Long, hrp: String): String

    fun verify(msg: ByteArray, signature: SignatureUnlock) {
        val a1: Long = signature.mNativeObj
        signature.mNativeObj = 0
        do_verify(mNativeObj, msg, a1)
       //TODO java.lang.ref.Reference.reachabilityFence(signature)
    }

    private external fun do_verify(self: Long, msg: ByteArray, signature: Long)

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

    private external fun do_delete(me: Long)

    /*package*/

    internal constructor(marker: InternalPointerMarker, ptr: Long){
        check(marker == InternalPointerMarker.RAW_PTR)
        mNativeObj = ptr
    }

    constructor()


    /*package*/
    var mNativeObj: Long = 0
}