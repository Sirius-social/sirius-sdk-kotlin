package org.iota.client

import kotlin.jvm.Synchronized


class MessageId {
    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is MessageId) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    override fun toString(): String {
        run { return this.to_string() }
    }

    constructor() {
        mNativeObj = init()
    }

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: MessageId): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO   java.lang.ref.Reference.reachabilityFence(o)
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
          //  super.finalize()
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
        private external fun init(): Long
        private external fun do_to_string(self: Long): String
        private external fun do_rustEq(self: Long, o: Long): Boolean

        /**
         * Create a MessageId from string
         */
        fun fromString(str_rep: String): MessageId {
            val ret = do_fromString(str_rep)
            return MessageId(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_fromString(str_rep: String): Long
        private external fun do_delete(me: Long)
    }
}