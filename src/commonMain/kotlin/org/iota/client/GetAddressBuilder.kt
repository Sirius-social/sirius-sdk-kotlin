package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Builder of GET /api/v1/address/{address} endpoint
 */
class GetAddressBuilder {
    private constructor() {}

    /**
     * Consume the builder and get the balance of a given Bech32 encoded address.
     * If count equals maxResults, then there might be more outputs available but those were skipped for performance
     * reasons. User should sweep the address to reduce the amount of outputs.
     * @param address The address to get the balance for
     */
    fun balance(address: String): BalanceAddressResponse {
        val ret = do_balance(mNativeObj, address)
        return BalanceAddressResponse(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Consume the builder and get all outputs that use a given address.
     * If count equals maxResults, then there might be more outputs available but those were skipped for performance
     * reasons. User should sweep the address to reduce the amount of outputs.
     * @param address The address to get the balance for
     * @param options The options for finding outputs
     */
    fun outputs(address: String, options: OutputsOptions): Array<UtxoInput> {
        val a1 = options.mNativeObj
        options.mNativeObj = 0
        val ret = do_outputs(mNativeObj, address, a1)
        //TODO  java.lang.ref.Reference.reachabilityFence(options)
        return ret
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
        //    super.finalize()
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
        private external fun do_balance(self: Long, address: String): Long
        private external fun do_outputs(
            self: Long,
            address: String,
            options: Long
        ): Array<UtxoInput>

        private external fun do_delete(me: Long)
    }
}