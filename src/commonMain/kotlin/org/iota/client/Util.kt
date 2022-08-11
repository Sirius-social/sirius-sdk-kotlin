package org.iota.client


object Util {
    /**
     * Function to consolidate all funds from a range of addresses to the address with the lowest index in that range
     * Returns the address to which the funds got consolidated, if any were available
     */
    fun consolidateFunds(
        client: org.iota.client.Client,
        seed: String,
        account_index: Long,
        address_range_low: Long,
        address_range_high: Long
    ): String {
        val a0: Long = client.mNativeObj
        client.mNativeObj = 0
        val ret =
            do_consolidateFunds(a0, seed, account_index, address_range_low, address_range_high)
        //TODO  java.lang.ref.Reference.reachabilityFence(client)
        return ret
    }

    private external fun do_consolidateFunds(
        client: Long,
        seed: String,
        account_index: Long,
        address_range_low: Long,
        address_range_high: Long
    ): String

    /**
     * Function to find the index and public or internal type of an Bech32 encoded address
     */
    fun searchAddress(
        seed: String,
        bech32_hrp: String,
        account_index: Long,
        range_low: Long,
        range_high: Long,
        address: Address
    ): org.iota.client.IndexPublicDto {
        val a5 = address.mNativeObj
        address.mNativeObj = 0
        val ret = do_searchAddress(seed, bech32_hrp, account_index, range_low, range_high, a5)
        val convRet: org.iota.client.IndexPublicDto =
            org.iota.client.IndexPublicDto(InternalPointerMarker.RAW_PTR, ret)
        //TODO   java.lang.ref.Reference.reachabilityFence(address)
        return convRet
    }

    private external fun do_searchAddress(
        seed: String,
        bech32_hrp: String,
        account_index: Long,
        range_low: Long,
        range_high: Long,
        address: Long
    ): Long
}