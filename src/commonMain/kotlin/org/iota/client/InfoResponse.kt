// Automatically generated by flapigen
package org.iota.client

import kotlin.jvm.Synchronized

/**
 * Response of GET /api/v1/info.
 * Returns general information about the node.
 */
class InfoResponse {
    override fun toString(): String {
        run { return this.to_string() }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is InfoResponse) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: InfoResponse): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO   java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * The name of the node
     */
    fun name(): String {
        return do_name(mNativeObj)
    }

    /**
     * The node software version
     */
    fun version(): String {
        return do_version(mNativeObj)
    }

    /**
     * The network id the node is connected to
     */
    fun networkId(): String {
        return do_networkId(mNativeObj)
    }

    /**
     * The bech32 HRP which is accepted by this node
     */
    fun bech32Hrp(): String {
        return do_bech32Hrp(mNativeObj)
    }

    /**
     * The messages per second this node is receiving
     */
    fun messagesPerSecond(): Double {
        return do_messagesPerSecond(mNativeObj)
    }

    /**
     * The referenced messages per second this node is receiving
     */
    fun referencedMessagesPerSecond(): Double {
        return do_referencedMessagesPerSecond(mNativeObj)
    }

    /**
     * The reference rate
     */
    fun referencedRate(): Double {
        return do_referencedRate(mNativeObj)
    }

    /**
     * The timestamp of the latest received milestone
     */
    fun latestMilestoneTimestamp(): Long {
        return do_latestMilestoneTimestamp(mNativeObj)
    }

    /**
     * The minimum required PoW for a message to be accepted
     */
    fun minPowScore(): Double {
        return do_minPowScore(mNativeObj)
    }

    /**
     * The index of the latest seen milestone
     */
    fun latestMilestoneIndex(): Long {
        return do_latestMilestoneIndex(mNativeObj)
    }

    /**
     * The index of the latest confirmed milestone
     */
    fun confirmedMilestoneIndex(): Long {
        return do_confirmedMilestoneIndex(mNativeObj)
    }

    /**
     * The milestone index this node is pruning from
     */
    fun pruningIndex(): Long {
        return do_pruningIndex(mNativeObj)
    }

    /**
     * List of features running on this node
     */
    fun features(): Array<String> {
        return do_features(mNativeObj)
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
        private external fun do_name(self: Long): String
        private external fun do_version(self: Long): String
        private external fun do_networkId(self: Long): String
        private external fun do_bech32Hrp(self: Long): String
        private external fun do_messagesPerSecond(self: Long): Double
        private external fun do_referencedMessagesPerSecond(self: Long): Double
        private external fun do_referencedRate(self: Long): Double
        private external fun do_latestMilestoneTimestamp(self: Long): Long
        private external fun do_minPowScore(self: Long): Double
        private external fun do_latestMilestoneIndex(self: Long): Long
        private external fun do_confirmedMilestoneIndex(self: Long): Long
        private external fun do_pruningIndex(self: Long): Long
        private external fun do_features(self: Long): Array<String>
        private external fun do_delete(me: Long)
    }
}