package org.iota.client

import kotlin.jvm.Synchronized

/**
 * Structure for sorting of UnlockBlocks
 */
class AddressIndexRecorder {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Index of the account
     */
    fun accountIndex(): Long {
        return do_accountIndex(mNativeObj)
    }

    /**
     * The input used
     */
    fun input(): org.iota.client.Input {
        val ret = do_input(mNativeObj)
        return org.iota.client.Input(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * The output information
     */
    fun output(): org.iota.client.OutputResponse {
        val ret = do_output(mNativeObj)
        return org.iota.client.OutputResponse(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * index of this address on the seed
     */
    fun addressIndex(): Long {
        return do_addressIndex(mNativeObj)
    }

    /**
     * The chain derived from seed
     */
    fun chain(): org.iota.client.Chain {
        val ret = do_chain(mNativeObj)
        return org.iota.client.Chain(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Whether this is an internal address
     */
    fun internal(): Boolean {
        return do_internal(mNativeObj)
    }

    /**
     * The address
     */
    fun bech32Address(): String {
        return do_bech32Address(mNativeObj)
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
            //super.finalize()
        }
    }

    /*package*/
    internal constructor(marker: InternalPointerMarker, ptr: Long) {
        check(marker == InternalPointerMarker.RAW_PTR)
        mNativeObj = ptr
    }

    /*package*/
    var mNativeObj: Long = 0

    companion object {
        private external fun do_to_string(self: Long): String
        private external fun do_accountIndex(self: Long): Long
        private external fun do_input(self: Long): Long
        private external fun do_output(self: Long): Long
        private external fun do_addressIndex(self: Long): Long
        private external fun do_chain(self: Long): Long
        private external fun do_internal(self: Long): Boolean
        private external fun do_bech32Address(self: Long): String
        private external fun do_delete(me: Long)
    }
}