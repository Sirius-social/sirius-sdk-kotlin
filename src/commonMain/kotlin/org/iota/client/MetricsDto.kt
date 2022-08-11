package org.iota.client

import kotlin.jvm.Synchronized

class MetricsDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The amount of new messages received
     */
    fun newMessages(): Long {
        return do_newMessages(mNativeObj)
    }

    /**
     * The amount of received messages
     */
    fun receivedMessages(): Long {
        return do_receivedMessages(mNativeObj)
    }

    /**
     * The amount of received known messages
     */
    fun knownMessages(): Long {
        return do_knownMessages(mNativeObj)
    }

    /**
     * The amount of received requested messages
     */
    fun receivedMessageRequests(): Long {
        return do_receivedMessageRequests(mNativeObj)
    }

    /**
     * The amount of received requested milestones
     */
    fun receivedMilestoneRequests(): Long {
        return do_receivedMilestoneRequests(mNativeObj)
    }

    /**
     * The amount of received heartbeats
     */
    fun receivedHeartbeats(): Long {
        return do_receivedHeartbeats(mNativeObj)
    }

    /**
     * The amount of sent messages
     */
    fun sentMessages(): Long {
        return do_sentMessages(mNativeObj)
    }

    /**
     * The amount of sent messages requests
     */
    fun sentMessageRequests(): Long {
        return do_sentMessageRequests(mNativeObj)
    }

    /**
     * The amount of sent milestone requests
     */
    fun sentMilestoneRequests(): Long {
        return do_sentMilestoneRequests(mNativeObj)
    }

    /**
     * The amount of sent heartbeats
     */
    fun sentHeartbeats(): Long {
        return do_sentHeartbeats(mNativeObj)
    }

    /**
     * The amount of dropped packets
     */
    fun droppedPackets(): Long {
        return do_droppedPackets(mNativeObj)
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
           // super.finalize()
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
        private external fun do_newMessages(self: Long): Long
        private external fun do_receivedMessages(self: Long): Long
        private external fun do_knownMessages(self: Long): Long
        private external fun do_receivedMessageRequests(self: Long): Long
        private external fun do_receivedMilestoneRequests(self: Long): Long
        private external fun do_receivedHeartbeats(self: Long): Long
        private external fun do_sentMessages(self: Long): Long
        private external fun do_sentMessageRequests(self: Long): Long
        private external fun do_sentMilestoneRequests(self: Long): Long
        private external fun do_sentHeartbeats(self: Long): Long
        private external fun do_droppedPackets(self: Long): Long
        private external fun do_delete(me: Long)
    }
}