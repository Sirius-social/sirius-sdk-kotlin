package org.iota.client

import kotlin.jvm.Synchronized


class HeartbeatDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The solid milestone index
     */
    fun solidMilestoneIndex(): Long {
        return do_solidMilestoneIndex(mNativeObj)
    }

    /**
     * The pruend milestone index
     */
    fun prunedMilestoneIndex(): Long {
        return do_prunedMilestoneIndex(mNativeObj)
    }

    /**
     * The latest milestone index
     */
    fun latestMilestoneIndex(): Long {
        return do_latestMilestoneIndex(mNativeObj)
    }

    /**
     * The amount of connected neighbors
     */
    fun connectedNeighbors(): Short {
        return do_connectedNeighbors(mNativeObj)
    }

    /**
     * The amount of synced neighbors
     */
    fun syncedNeighbors(): Short {
        return do_syncedNeighbors(mNativeObj)
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
      //      super.finalize()
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
        private external fun do_solidMilestoneIndex(self: Long): Long
        private external fun do_prunedMilestoneIndex(self: Long): Long
        private external fun do_latestMilestoneIndex(self: Long): Long
        private external fun do_connectedNeighbors(self: Long): Short
        private external fun do_syncedNeighbors(self: Long): Short
        private external fun do_delete(me: Long)
    }
}