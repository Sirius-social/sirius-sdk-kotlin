package org.iota.client

import kotlin.jvm.Synchronized


class UnlockBlock {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    fun kind(): UnlockBlockKind {
        val ret = do_kind(mNativeObj)
        return UnlockBlockKind.fromInt(ret)
    }

    fun asReference(): org.iota.client.ReferenceUnlock {
        val ret = do_asReference(mNativeObj)
        return org.iota.client.ReferenceUnlock(InternalPointerMarker.RAW_PTR, ret)
    }

    fun asSignature(): org.iota.client.SignatureUnlock {
        val ret = do_asSignature(mNativeObj)
        return org.iota.client.SignatureUnlock(InternalPointerMarker.RAW_PTR, ret)
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
        private external fun do_kind(self: Long): Int
        private external fun do_asReference(self: Long): Long
        private external fun do_asSignature(self: Long): Long
        private external fun do_delete(me: Long)
    }
}