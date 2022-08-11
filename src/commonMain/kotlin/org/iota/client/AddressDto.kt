package org.iota.client

import kotlin.jvm.Synchronized

class AddressDto {
    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is AddressDto) equal = obj.rustEq(this)
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

    private fun rustEq(o: AddressDto): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
       //TODO  java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    private external fun do_rustEq(self: Long, o: Long): Boolean

    /**
     * The kind of address
     */
    fun kind(): Short {
        return do_kind(mNativeObj)
    }

    private external fun do_kind(self: Long): Short

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
          //  super.finalize()
        }
    }

    private external fun do_delete(me: Long)

    /*package*/


    internal constructor(marker: InternalPointerMarker, ptr: Long){
        check(marker == InternalPointerMarker.RAW_PTR)
        mNativeObj = ptr
    }
    constructor(){

    }


    /*package*/
    var mNativeObj: Long = 0
}