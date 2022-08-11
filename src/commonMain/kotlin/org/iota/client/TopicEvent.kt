package org.iota.client

import kotlin.jvm.Synchronized


class TopicEvent {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * the MQTT topic.
     */
    fun topic(): String {
        return do_topic(mNativeObj)
    }

    /**
     * The MQTT event payload.
     */
    fun payload(): String {
        return do_payload(mNativeObj)
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
        private external fun do_topic(self: Long): String
        private external fun do_payload(self: Long): String
        private external fun do_delete(me: Long)
    }
}