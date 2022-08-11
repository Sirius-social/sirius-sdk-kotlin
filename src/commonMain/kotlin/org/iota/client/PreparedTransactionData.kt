package org.iota.client

import kotlin.jvm.Synchronized

/**
 * Helper struct for offline signing
 */
class PreparedTransactionData {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Transaction essence
     */
    fun essence(): org.iota.client.Essence {
        val ret = do_essence(mNativeObj)
        return org.iota.client.Essence(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Required address information for signing
     */
    fun addressIndexRecorders(): Array<AddressIndexRecorder> {
        return do_addressIndexRecorders(mNativeObj)
    }

    /**
     * Serializes the prepared data into a json string
     */
    fun serialize(): String {
        return do_serialize(mNativeObj)
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

        /**
         * Turns a serialized preparedTransactionData string back into its class
         * @param serialised_data The serialised transaction data
         */
        fun deserialize(serialised_data: String): PreparedTransactionData {
            val ret =
                do_deserialize(serialised_data)
            return PreparedTransactionData(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_deserialize(serialised_data: String): Long
        private external fun do_essence(self: Long): Long
        private external fun do_addressIndexRecorders(self: Long): Array<AddressIndexRecorder>
        private external fun do_serialize(self: Long): String
        private external fun do_delete(me: Long)
    }
}