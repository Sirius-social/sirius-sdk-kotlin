package org.iota.client

import kotlin.jvm.Synchronized


class GetAddressesBuilder {
    private constructor() {}

    /**
     * Set the account index
     */
    fun withAccountIndex(account_index: Long): GetAddressesBuilder {
        val ret =
            do_withAccountIndex(mNativeObj, account_index)
        return GetAddressesBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Set range to the builder
     */
    fun withRange(start: Long, end: Long): GetAddressesBuilder {
        val ret = do_withRange(mNativeObj, start, end)
        return GetAddressesBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Set bech32 human readable part (hrp)
     */
    fun withBech32Hrp(bech32_hrp: String): GetAddressesBuilder {
        val ret =
            do_withBech32Hrp(mNativeObj, bech32_hrp)
        return GetAddressesBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Set client to the builder
     */
    fun withClient(client: Client): GetAddressesBuilder {
        val a0 = client.mNativeObj
        val ret = do_withClient(mNativeObj, a0)
        val convRet = GetAddressesBuilder(InternalPointerMarker.RAW_PTR, ret)
        //TODO  java.lang.ref.Reference.reachabilityFence(client)
        return convRet
    }

    /**
     * Consume the builder and get a vector of public addresses bech32 encoded
     */
    fun finish(): Array<String> {
        return do_finish(mNativeObj)
    }

    val all: Array<AddressStringPublicWrapper>
        get() = do_getAll(mNativeObj)

    /**
     * Consume the builder and get the vector of public and internal addresses
     */
    val allRaw: Array<AddressPublicWrapper>
        get() = do_getAllRaw(mNativeObj)

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
        /**
         * Construct a new addressbuilder with a seed. Invalid seeds throw an error
         */
        fun from(seed: String): GetAddressesBuilder {
            val ret = do_from(seed)
            return GetAddressesBuilder(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_from(seed: String): Long

        /**
         * DEBUG DO NOT USE
         */
        fun fromOld(seed: String): GetAddressesBuilder {
            val ret = do_fromOld(seed)
            return GetAddressesBuilder(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_fromOld(seed: String): Long
        private external fun do_withAccountIndex(self: Long, account_index: Long): Long
        private external fun do_withRange(self: Long, start: Long, end: Long): Long
        private external fun do_withBech32Hrp(self: Long, bech32_hrp: String): Long
        private external fun do_withClient(self: Long, client: Long): Long
        private external fun do_finish(self: Long): Array<String>
        private external fun do_getAll(self: Long): Array<AddressStringPublicWrapper>
        private external fun do_getAllRaw(self: Long): Array<AddressPublicWrapper>
        private external fun do_delete(me: Long)
    }
}