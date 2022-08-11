package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Represent the object that nodes gossip around the network.
 */
class Message {
    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is Message) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: Message): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
        //TODO    java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    /**
     * Serializes the receipt payload into a json string
     */
    fun serialize(): String {
        return do_serialize(mNativeObj)
    }

    /**
     * Returns the network id of a `Message`.
     */
    fun networkId(): Long {
        return do_networkId(mNativeObj)
    }

    /**
     * Computes the identifier of the message.
     */
    fun id(): org.iota.client.MessageId {
        val ret = do_id(mNativeObj)
        return org.iota.client.MessageId(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Returns the nonce of a `Message`.
     */
    fun nonce(): Long {
        return do_nonce(mNativeObj)
    }

    /**
     * Returns the parents of a `Message`.
     */
    fun parents(): Array<org.iota.client.MessageId> {
        return do_parents(mNativeObj)
    }

    /**
     * Returns the optional payload of a `Message`.
     */
    fun payload(): MessagePayload? {
        val ret = do_payload(mNativeObj)
        val convRet: MessagePayload? = if (ret != 0L) {
            MessagePayload(
                    InternalPointerMarker.RAW_PTR,
                    ret
                )
        } else {
            MessagePayload()
        }
        return convRet
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
        private external fun do_to_string(self: Long): String
        private external fun do_rustEq(self: Long, o: Long): Boolean
        private external fun do_serialize(self: Long): String

        /**
         * Turns a serialized receipt payload string back into its class
         */
        fun deserialize(serialised_data: String): Message {
            val ret = do_deserialize(serialised_data)
            return Message(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_deserialize(serialised_data: String): Long

        /**
         * Creates a new `MessageBuilder` to construct an instance of a `Message`.
         */
        fun builder(): org.iota.client.MessageBuilder {
            val ret = do_builder()
            return org.iota.client.MessageBuilder(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_builder(): Long
        private external fun do_networkId(self: Long): Long
        private external fun do_id(self: Long): Long
        private external fun do_nonce(self: Long): Long
        private external fun do_parents(self: Long): Array<org.iota.client.MessageId>
        private external fun do_payload(self: Long): Long
        private external fun do_delete(me: Long)
    }
}