package org.iota.client

import kotlin.jvm.Synchronized

class Input {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is Input) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: Input): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO   java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    fun kind(): org.iota.client.InputKind {
        val ret = do_kind(mNativeObj)
        return InputKind.fromInt(ret)
    }

    fun asUtxo(): UtxoInput {
        val ret = do_asUtxo(mNativeObj)
        return UtxoInput(InternalPointerMarker.RAW_PTR, ret)
    }

    fun asTreasury(): TreasuryInput {
        val ret = do_asTreasury(mNativeObj)
        return TreasuryInput(InternalPointerMarker.RAW_PTR, ret)
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
    var mNativeObj: Long = 0

    companion object {
        private external fun do_to_string(self: Long): String
        private external fun do_rustEq(self: Long, o: Long): Boolean
        private external fun do_kind(self: Long): Int
        private external fun do_asUtxo(self: Long): Long
        private external fun do_asTreasury(self: Long): Long
        private external fun do_delete(me: Long)
    }
}