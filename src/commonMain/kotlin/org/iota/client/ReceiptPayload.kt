package org.iota.client

import kotlin.jvm.Synchronized


class ReceiptPayload {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Serializes the receipt payload into a json string
     */
    fun serialize(): String {
        return do_serialize(mNativeObj)
    }

    /**
     * Returns the milestone index at which the funds of a `ReceiptPayload` were migrated at in the legacy network.
     */
    fun migratedAt(): Long {
        return do_migratedAt(mNativeObj)
    }

    /**
     * Returns whether a `ReceiptPayload` is the final one for a given migrated at index.
     */
    fun last(): Boolean {
        return do_last(mNativeObj)
    }

    /**
     * The funds which were migrated with a `ReceiptPayload`.
     */
    fun funds(): Array<org.iota.client.MigratedFundsEntry> {
        return do_funds(mNativeObj)
    }

    /**
     * The `TreasuryTransaction` used to fund the funds of a `ReceiptPayload`.
     */
    fun transaction(): TreasuryPayload {
        val ret = do_transaction(mNativeObj)
        return TreasuryPayload(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Returns the sum of all `MigratedFundsEntry` items within a `ReceiptPayload`.
     */
    fun amount(): Long {
        return do_amount(mNativeObj)
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
        /**
         * Creates a new `ReceiptPayload`.
         */
        fun from(
            migrated_at: Long,
            last: Boolean,
            funds: Array<org.iota.client.MigratedFundsEntry>,
            transaction: org.iota.client.MessagePayload
        ): ReceiptPayload {
            val a3: Long = transaction.mNativeObj
            transaction.mNativeObj = 0
            val ret = do_from(migrated_at, last, funds, a3)
            val convRet = ReceiptPayload(InternalPointerMarker.RAW_PTR, ret)
            //TODO  java.lang.ref.Reference.reachabilityFence(transaction)
            return convRet
        }

        private external fun do_from(
            migrated_at: Long,
            last: Boolean,
            funds: Array<org.iota.client.MigratedFundsEntry>,
            transaction: Long
        ): Long

        private external fun do_to_string(self: Long): String
        private external fun do_serialize(self: Long): String

        /**
         * Turns a serialized receipt payload string back into its class
         */
        fun deserialize(serialised_data: String): ReceiptPayload {
            val ret = do_deserialize(serialised_data)
            return ReceiptPayload(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_deserialize(serialised_data: String): Long
        private external fun do_migratedAt(self: Long): Long
        private external fun do_last(self: Long): Boolean
        private external fun do_funds(self: Long): Array<org.iota.client.MigratedFundsEntry>
        private external fun do_transaction(self: Long): Long
        private external fun do_amount(self: Long): Long
        private external fun do_delete(me: Long)
    }
}