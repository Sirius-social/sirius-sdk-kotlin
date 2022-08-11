package org.iota.client

import kotlin.jvm.Synchronized


class TransactionPayload {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Serializes the transaction payload into a json string
     */
    fun serialize(): String {
        return do_serialize(mNativeObj)
    }

    /**
     * Return the essence of a `TransactionPayload`.
     */
    fun essence(): org.iota.client.Essence {
        val ret = do_essence(mNativeObj)
        return org.iota.client.Essence(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Computes the identifier of a `TransactionPayload`.
     */
    fun id(): org.iota.client.TransactionId {
        val ret = do_id(mNativeObj)
        return org.iota.client.TransactionId(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Return unlock blocks of a `TransactionPayload`.
     */
    fun unlockBlocks(): Array<UnlockBlock> {
        return do_unlockBlocks(mNativeObj)
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
        private external fun do_serialize(self: Long): String

        /**
         * Turns a serialized transaction payload string back into its class
         */
        fun deserialize(serialised_data: String): TransactionPayload {
            val ret = do_deserialize(serialised_data)
            return TransactionPayload(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_deserialize(serialised_data: String): Long

        /**
         * Return a new `TransactionPayloadBuilder` to build a `TransactionPayload`.
         */
        fun builder(): TransactionPayloadBuilder {
            val ret = do_builder()
            return TransactionPayloadBuilder(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_builder(): Long
        private external fun do_essence(self: Long): Long
        private external fun do_id(self: Long): Long
        private external fun do_unlockBlocks(self: Long): Array<UnlockBlock>
        private external fun do_delete(me: Long)
    }
}