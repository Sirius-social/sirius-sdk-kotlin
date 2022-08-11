package org.iota.client

import kotlin.jvm.Synchronized

class GossipDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The hearthbeat information
     */
    fun heartbeat(): org.iota.client.HeartbeatDto {
        val ret = do_heartbeat(mNativeObj)
        return org.iota.client.HeartbeatDto(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * The metrics information
     */
    fun metrics(): org.iota.client.MetricsDto {
        val ret = do_metrics(mNativeObj)
        return org.iota.client.MetricsDto(InternalPointerMarker.RAW_PTR, ret)
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
       //     super.finalize()
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
        private external fun do_heartbeat(self: Long): Long
        private external fun do_metrics(self: Long): Long
        private external fun do_delete(me: Long)
    }
}