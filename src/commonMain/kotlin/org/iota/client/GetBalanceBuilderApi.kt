package org.iota.client

import kotlin.jvm.Synchronized


class GetBalanceBuilderApi {
    private constructor() {}

    /**
     * Sets the account index.
     * @param account_index The account index to use (Default: 0)
     */
    fun withAccountIndex(account_index: Long): GetBalanceBuilderApi {
        val ret =
            do_withAccountIndex(mNativeObj, account_index)
        return GetBalanceBuilderApi(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Sets the index of the address to start looking for balance.
     * @param initial_address_index The starting index to use for searching addresses (Default: 0)
     */
    fun withInitialAddressIndex(initial_address_index: Long): GetBalanceBuilderApi {
        val ret = do_withInitialAddressIndex(
            mNativeObj,
            initial_address_index
        )
        return GetBalanceBuilderApi(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Sets the gap limit to specify how many addresses will be checked each round.
     * If gap_limit amount of addresses in a row have no balance the function will return.
     * @param gap_limit The gap limit (Default: 20)
     */
    fun withGapLimit(gap_limit: Long): GetBalanceBuilderApi {
        val ret =
            do_withGapLimit(mNativeObj, gap_limit)
        return GetBalanceBuilderApi(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Consume the builder and get the API result
     */
    fun finish(): Long {
        return do_finish(mNativeObj)
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
        private external fun do_withAccountIndex(self: Long, account_index: Long): Long
        private external fun do_withInitialAddressIndex(
            self: Long,
            initial_address_index: Long
        ): Long

        private external fun do_withGapLimit(self: Long, gap_limit: Long): Long
        private external fun do_finish(self: Long): Long
        private external fun do_delete(me: Long)
    }
}