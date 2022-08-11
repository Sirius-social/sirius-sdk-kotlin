package org.iota.client

import kotlin.jvm.Synchronized


class Topic {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
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
    var mNativeObj: Long = 0

    companion object {
        /**
         * Creates a new topic and checks if it's valid.
         */
        fun from(topic: String): Topic {
            val ret = do_from(topic)
            return Topic(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_from(topic: String): Long
        private external fun do_to_string(self: Long): String
        private external fun do_delete(me: Long)
    }
}