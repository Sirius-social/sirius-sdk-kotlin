// Automatically generated by flapigen
package org.iota.client

import kotlin.jvm.Synchronized

/**
 * The iota.rs client instance
 */
class Client {
    private constructor() {}

    /**
     * GET /health endpoint
     */
    val health: Boolean
        get() = do_getHealth(mNativeObj)

    /**
     * GET /health endpoint for the passed node
     * @param node the node url which you want to query speicifically for, in the case of multiple nodes in a pool
     */
    fun getNodeHealth(node: String): Boolean {
        return do_getNodeHealth(mNativeObj, node)
    }

    val node: Node
        get() {
            val ret = do_getNode(mNativeObj)
            return Node(InternalPointerMarker.RAW_PTR, ret)
        }

    /**
     * Gets the network id of the node we're connecting to.
     */
    val networkId: Long
        get() = do_getNetworkId(mNativeObj)

    /**
     * Gets the miner to use based on the PoW setting
     */
    val powProvider: org.iota.client.ClientMiner
        get() {
            val ret = do_getPowProvider(mNativeObj)
            return org.iota.client.ClientMiner(InternalPointerMarker.RAW_PTR, ret)
        }

    /**
     * Gets the network related information such as network_id and min_pow_score
     * and if it's the default one, sync it first.
     */
    val networkInfo: NetworkInfo
        get() {
            val ret = do_getNetworkInfo(mNativeObj)
            return NetworkInfo(InternalPointerMarker.RAW_PTR, ret)
        }

    /**
     * GET /api/v1/info endpoint
     */
    val info: NodeInfoWrapper
        get() {
            val ret = do_getInfo(mNativeObj)
            return NodeInfoWrapper(InternalPointerMarker.RAW_PTR, ret)
        }

    /**
     * GET /api/v1/peers endpoint
     */
    val peers: Array<PeerDto>
        get() = do_getPeers(mNativeObj)

    /**
     * GET /api/v1/tips endpoint
     */
    val tips: Array<String>
        get() = do_getTips(mNativeObj)

    /**
     * GET /api/v1/outputs/{outputId} endpoint
     * Find an output by its transaction_id and corresponding output_index.
     * @param output_id The id of the output
     */
    fun getOutput(output_id: String): OutputResponse {
        val ret = do_getOutput(mNativeObj, output_id)
        return OutputResponse(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * GET /api/v1/addresses/{address} endpoint
     * Creates a new instance of the AddressBuilder
     */
    val address: org.iota.client.GetAddressBuilder
        get() {
            val ret = do_getAddress(mNativeObj)
            return org.iota.client.GetAddressBuilder(InternalPointerMarker.RAW_PTR, ret)
        }

    /**
     * Return the balance in iota for the given address; No seed needed to do this
     * since we are only checking and already know the address.
     * @param address The address we want to get the balance for
     */
    fun getAddressBalance(address: String): BalanceAddressResponse {
        val ret =
            do_getAddressBalance(mNativeObj, address)
        return BalanceAddressResponse(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * since we are only checking and already know the addresses.
     * @param address The addresses we want to get the balance for
     */
    fun getAddressesBalances(addresses: Array<String>): Array<BalanceAddressResponse> {
        return do_getAddressesBalances(mNativeObj, addresses)
    }

    /**
     * Find all outputs based on the requests criteria. This method will try to query multiple nodes if
     * the request amount exceeds individual node limit.
     * @param output_ids The optional output ids to check for
     * @param addresses The optional list of addresses to check for
     */
    fun findOutputs(
        output_ids: Array<String>,
        addresses: Array<String>
    ): Array<OutputResponse> {
        return do_findOutputs(mNativeObj, output_ids, addresses)
    }

    /**
     * GET /api/v1/milestones/{index} endpoint
     * Get the milestone by the given index.
     * @param index the milestone index
     */
    fun getMilestone(index: Long): org.iota.client.MilestoneResponse {
        val ret = do_getMilestone(mNativeObj, index)
        return org.iota.client.MilestoneResponse(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * GET /api/v1/milestones/{index}/utxo-changes endpoint
     * Gets the utxo changes by the given milestone index.
     * @param index the milestone index
     */
    fun getMilestoneUtxoChanges(index: Long): org.iota.client.MilestoneUtxoChangesResponse {
        val ret =
            do_getMilestoneUtxoChanges(mNativeObj, index)
        return org.iota.client.MilestoneUtxoChangesResponse(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * GET /api/v1/receipts endpoint
     * Get all receipts.
     */
    val receipts: Array<ReceiptDto>
        get() = do_getReceipts(mNativeObj)

    /**
     * GET /api/v1/receipts/{migratedAt} endpoint
     * Get the receipts by the given milestone index.
     * @param index the milestone index
     */
    fun getReceiptsMigratedAt(index: Long): Array<ReceiptDto> {
        return do_getReceiptsMigratedAt(mNativeObj, index)
    }

    /**
     * GET /api/v1/treasury endpoint
     * Get the treasury output.
     */
    val treasury: TreasuryResponse
        get() {
            val ret = do_getTreasury(mNativeObj)
            return TreasuryResponse(InternalPointerMarker.RAW_PTR, ret)
        }

    /**
     * GET /api/v1/transactions/{transactionId}/included-message
     * Returns the included message of the transaction.
     * @param transaction_id the transaction id
     */
    fun getIncludedMessage(transaction_id: TransactionId): org.iota.client.Message {
        val a0 = transaction_id.mNativeObj
        transaction_id.mNativeObj = 0
        val ret = do_getIncludedMessage(mNativeObj, a0)
        val convRet: org.iota.client.Message =
            org.iota.client.Message(InternalPointerMarker.RAW_PTR, ret)
        //TODO java.lang.ref.Reference.reachabilityFence(transaction_id)
        return convRet
    }

    /**
     * POST /api/v1/messages endpoint
     * @param msg The message to post. Use `Client.message()` to create one.
     */
    fun postMessage(msg: org.iota.client.Message): org.iota.client.MessageId {
        val a0: Long = msg.mNativeObj
        msg.mNativeObj = 0
        val ret = do_postMessage(mNativeObj, a0)
        val convRet: org.iota.client.MessageId =
            org.iota.client.MessageId(InternalPointerMarker.RAW_PTR, ret)
      //TODO  java.lang.ref.Reference.reachabilityFence(msg)
        return convRet
    }

    /**
     * Reattaches messages for provided message id. Messages can be reattached only if they are valid and haven't been
     * confirmed for a while.
     * @param message_id The id of the Message to reattach
     */
    fun reattach(message_id: org.iota.client.MessageId): org.iota.client.MessageWrap {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret = do_reattach(mNativeObj, a0)
        val convRet: org.iota.client.MessageWrap =
            org.iota.client.MessageWrap(InternalPointerMarker.RAW_PTR, ret)
     //TODO   java.lang.ref.Reference.reachabilityFence(message_id)
        return convRet
    }

    /**
     * Reattach a message without checking if it should be reattached
     * @param message_id The id of the Message to reattach
     */
    fun reattachUnchecked(message_id: org.iota.client.MessageId): org.iota.client.MessageWrap {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret = do_reattachUnchecked(mNativeObj, a0)
        val convRet: org.iota.client.MessageWrap =
            org.iota.client.MessageWrap(InternalPointerMarker.RAW_PTR, ret)
      //TODO  java.lang.ref.Reference.reachabilityFence(message_id)
        return convRet
    }

    /**
     * Promotes a message. The method should validate if a promotion is necessary through get_message. If not, the
     * method should error out and should not allow unnecessary promotions.
     * @param message_id The id of the Message to promote
     */
    fun promote(message_id: org.iota.client.MessageId): org.iota.client.MessageWrap {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret = do_promote(mNativeObj, a0)
        val convRet: org.iota.client.MessageWrap =
            org.iota.client.MessageWrap(InternalPointerMarker.RAW_PTR, ret)
    //TODO    java.lang.ref.Reference.reachabilityFence(message_id)
        return convRet
    }

    /**
     * Promote a message without checking if it should be promoted
     * @param message_id The id of the Message to promote
     */
    fun promoteUnchecked(message_id: org.iota.client.MessageId): org.iota.client.MessageWrap {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret = do_promoteUnchecked(mNativeObj, a0)
        val convRet: org.iota.client.MessageWrap =
            org.iota.client.MessageWrap(InternalPointerMarker.RAW_PTR, ret)
        //TODO      java.lang.ref.Reference.reachabilityFence(message_id)
        return convRet
    }

    /**
     * Return the balance for a provided seed
     * Addresses with balance must be consecutive, so this method will return once it encounters a zero
     * balance address.
     * @param seed the seed which contains the addressses you want to check balance for
     */
    fun getBalance(seed: String): org.iota.client.GetBalanceBuilderApi {
        val ret = do_getBalance(mNativeObj, seed)
        return org.iota.client.GetBalanceBuilderApi(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * A generic send function for easily sending transaction or indexation messages.
     */
    fun message(): org.iota.client.ClientMessageBuilder {
        val ret = do_message(mNativeObj)
        return org.iota.client.ClientMessageBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * GET /api/v1/messages/{messageId} endpoint
     */
    val message: org.iota.client.GetMessageBuilder
        get() {
            val ret = do_getMessage(mNativeObj)
            return org.iota.client.GetMessageBuilder(InternalPointerMarker.RAW_PTR, ret)
        }

    /**
     * Return a list of addresses from the seed regardless of their validity.
     * @param the ssed that will create the addresses
     */
    fun getAddresses(seed: String): org.iota.client.GetAddressesBuilder {
        val ret = do_getAddresses(mNativeObj, seed)
        return org.iota.client.GetAddressesBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Retries (promotes or reattaches) a message for provided message id until it's included (referenced by a
     * milestone). Default interval is 5 seconds and max attempts is 40. Returns reattached messages. Set to -1 for defaults.
     *
     * @param message_id The id of the Message to include
     * @param interval The interval in seconds to try
     * @param max_attempts The maximum attempts for retrying
     */
    fun retryUntilIncluded(
        message_id: org.iota.client.MessageId,
        interval: Long,
        max_attempts: Long
    ): Array<org.iota.client.MessageWrap> {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret: Array<org.iota.client.MessageWrap> =
            do_retryUntilIncluded(mNativeObj, a0, interval, max_attempts)
        //TODO  java.lang.ref.Reference.reachabilityFence(message_id)
        return ret
    }

    /**
     * Returns a handle to the MQTT topics manager.
     */
    fun subscriber(): org.iota.client.MqttManager {
        val ret = do_subscriber(mNativeObj)
        return org.iota.client.MqttManager(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Function to find inputs from addresses for a provided amount (useful for offline signing)
     * @param addresses The addresses to obtain balance from
     * @param amount the amount we need to find
     */
    fun findInputs(
        addresses: Array<String>,
        amount: Long
    ): Array<UtxoInput> {
        return do_findInputs(mNativeObj, addresses, amount)
    }

    /**
     * Transforms a hex encoded address to a bech32 encoded address
     * @param hex The address Bech32 string
     * @param bech32_hrp The Bech32 hrp string
     */
    fun hexToBech32(hex: String, bech32_hrp: String): String {
        return do_hexToBech32(mNativeObj, hex, bech32_hrp)
    }

    /**
     * Transforms a hex encoded public key to a bech32 encoded address
     * @param hex hex encoded public key
     * @param bech32_hrp The Bech32 hrp string
     */
    fun hexPublicKeyToBech32Address(hex: String, bech32_hrp: String): String {
        return do_hexPublicKeyToBech32Address(mNativeObj, hex, bech32_hrp)
    }

    /**
     * Temporarily method to check if your seed is made using the incorrect generation of the old JAVA seed input
     * @param seed The seed you used previously
     * @param account_index The account index used, is 0 when you didnt use it
     * @param address_index The address index you want to migrate
     * @param pub_addr If it's a public or internal address
     */
    fun shouldMigrate(
        seed: String,
        account_index: Long,
        address_index: Long,
        pub_addr: Boolean
    ): Boolean {
        return do_shouldMigrate(mNativeObj, seed, account_index, address_index, pub_addr)
    }

    /**
     * Temporarily method in order to migrate wrongly generated seeds from JAVA to Rust
     * Migrates the balance of the address towards the provided to_address
     * And returns the message or an error
     * @param seed The seed you used previously
     * @param account_index The account index used, is 0 when you didnt use it
     * @param address_index The address index you want to migrate
     * @param pub_addr If it's a public or internal address
     * @param to_address The address we send the balance to
     */
    fun migrate(
        seed: String,
        account_index: Long,
        address_index: Long,
        pub_addr: Boolean,
        to_address: String
    ): org.iota.client.Message {
        val ret = do_migrate(
            mNativeObj,
            seed,
            account_index,
            address_index,
            pub_addr,
            to_address
        )
        return org.iota.client.Message(InternalPointerMarker.RAW_PTR, ret)
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
           // super.finalize()
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
         * Creates a new instance of the CLient builder
         */
        fun Builder(): org.iota.client.ClientBuilder {
            val ret = do_Builder()
            return org.iota.client.ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_Builder(): Long
        private external fun do_getHealth(self: Long): Boolean
        private external fun do_getNodeHealth(self: Long, node: String): Boolean
        private external fun do_getNode(self: Long): Long
        private external fun do_getNetworkId(self: Long): Long
        private external fun do_getPowProvider(self: Long): Long
        private external fun do_getNetworkInfo(self: Long): Long
        private external fun do_getInfo(self: Long): Long
        private external fun do_getPeers(self: Long): Array<PeerDto>
        private external fun do_getTips(self: Long): Array<String>
        private external fun do_getOutput(self: Long, output_id: String): Long
        private external fun do_getAddress(self: Long): Long
        private external fun do_getAddressBalance(self: Long, address: String): Long
        private external fun do_getAddressesBalances(
            self: Long,
            addresses: Array<String>
        ): Array<BalanceAddressResponse>

        private external fun do_findOutputs(
            self: Long,
            output_ids: Array<String>,
            addresses: Array<String>
        ): Array<OutputResponse>

        private external fun do_getMilestone(self: Long, index: Long): Long
        private external fun do_getMilestoneUtxoChanges(self: Long, index: Long): Long
        private external fun do_getReceipts(self: Long): Array<ReceiptDto>
        private external fun do_getReceiptsMigratedAt(self: Long, index: Long): Array<ReceiptDto>
        private external fun do_getTreasury(self: Long): Long
        private external fun do_getIncludedMessage(self: Long, transaction_id: Long): Long
        private external fun do_postMessage(self: Long, msg: Long): Long
        private external fun do_reattach(self: Long, message_id: Long): Long
        private external fun do_reattachUnchecked(self: Long, message_id: Long): Long
        private external fun do_promote(self: Long, message_id: Long): Long
        private external fun do_promoteUnchecked(self: Long, message_id: Long): Long
        private external fun do_getBalance(self: Long, seed: String): Long
        private external fun do_message(self: Long): Long
        private external fun do_getMessage(self: Long): Long
        private external fun do_getAddresses(self: Long, seed: String): Long
        private external fun do_retryUntilIncluded(
            self: Long,
            message_id: Long,
            interval: Long,
            max_attempts: Long
        ): Array<org.iota.client.MessageWrap>

        private external fun do_subscriber(self: Long): Long

        /**
         * Generates a new mnemonic.
         */
        external fun generateMnemonic(): String?

        /**
         * Returns a hex encoded seed for a mnemonic.
         * @param mnemonic The mmnemonic to turn into a seed
         */
        external fun mnemonicToHexSeed(mnemonic: String?): String?
        private external fun do_findInputs(
            self: Long,
            addresses: Array<String>,
            amount: Long
        ): Array<UtxoInput>

        /**
         * Returns a parsed hex String from bech32.
         * @param bech32 The address Bech32 string
         */
        external fun bech32ToHex(bech32: String?): String?
        private external fun do_hexToBech32(self: Long, hex: String, bech32_hrp: String): String
        private external fun do_hexPublicKeyToBech32Address(
            self: Long,
            hex: String,
            bech32_hrp: String
        ): String

        /**
         * Checks if a str is a valid bech32 encoded address.
         * @param address The addresss string to check
         */
        external fun isAddressValid(address: String?): Boolean

        /**
         * Returns a valid Address parsed from a String.
         * @param address The addresss string to parse
         */
        fun parseBech32Address(address: String): Address {
            val ret =
                do_parseBech32Address(address)
            return Address(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_parseBech32Address(address: String): Long
        private external fun do_shouldMigrate(
            self: Long,
            seed: String,
            account_index: Long,
            address_index: Long,
            pub_addr: Boolean
        ): Boolean

        private external fun do_migrate(
            self: Long,
            seed: String,
            account_index: Long,
            address_index: Long,
            pub_addr: Boolean,
            to_address: String
        ): Long

        private external fun do_delete(me: Long)
    }
}