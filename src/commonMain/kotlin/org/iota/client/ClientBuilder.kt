// Automatically generated by flapigen
package org.iota.client

import kotlin.jvm.Synchronized

/**
 * The options builder for a client connected to multiple nodes.
 */
class ClientBuilder {
    /**
     * Create a new instance of the Client
     */
    constructor() {
        mNativeObj = init()
    }

    /**
     * Adds an IOTA node by its URL.
     * @param node The node URL
     */
    fun withNode(node: String): ClientBuilder {
        val ret = do_withNode(mNativeObj, node)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Adds a list of IOTA nodes by their URLs.
     * @param nodes The list of node URLs
     */
    fun withNodes(nodes: Array<String>): ClientBuilder {
        val ret = do_withNodes(mNativeObj, nodes)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Adds an IOTA node by its URL with optional jwt and or basic authentication
     * @param node The node URL
     * @param jwt The JWT, can be `null`
     * @param username The username, can be `null`
     * @param password The password, can be `null`. Only checked if username is not `null`
     */
    fun withNodeAuth(
        node: String,
        jwt: String,
        username: String,
        password: String
    ): ClientBuilder {
        val ret =
            do_withNodeAuth(mNativeObj, node, jwt, username, password)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Adds an IOTA node by its URL to be used as primary node, with optional jwt and or basic authentication
     * @param node The node URL
     * @param jwt The JWT, can be `null`
     * @param username The username, can be `null`
     * @param password The password, can be `null`. Only checked if username is not `null`
     */
    fun withPrimaryNode(
        node: String,
        jwt: String,
        username: String,
        password: String
    ): ClientBuilder {
        val ret =
            do_withPrimaryNode(mNativeObj, node, jwt, username, password)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Adds an IOTA node by its URL to be used as primary PoW node (for remote PoW), with optional jwt and or basic
     * authentication
     * @param node The node URL
     * @param jwt The JWT, can be `null`
     * @param username The username, can be `null`
     * @param password The password, can be `null`. Only checked if username is not `null`
     */
    fun withPrimaryPowNode(
        node: String,
        jwt: String,
        username: String,
        password: String
    ): ClientBuilder {
        val ret = do_withPrimaryPowNode(
            mNativeObj,
            node,
            jwt,
            username,
            password
        )
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Adds an IOTA permanode by its URL, with optional jwt and or basic authentication
     * @param node The node URL
     * @param jwt The JWT, can be `null`
     * @param username The username, can be `null`
     * @param password The password, can be `null`. Only checked if username is not `null`
     */
    fun withPermanode(
        node: String,
        jwt: String,
        username: String,
        password: String
    ): ClientBuilder {
        val ret =
            do_withPermanode(mNativeObj, node, jwt, username, password)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Get node list from the node_pool_urls
     * @param nodes node_pool_urls list of node URLs for the node pool
     */
    fun withNodePoolUrls(node_pool_urls: Array<String>): ClientBuilder {
        val ret =
            do_withNodePoolUrls(mNativeObj, node_pool_urls)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Allows creating the client without nodes for offline address generation or signing
     */
    fun withOfflineMode(): ClientBuilder {
        val ret = do_withOfflineMode(mNativeObj)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Client connected to the default Network node pool unless other nodes are provided.
     *
     * ```
     * import org.iota.client.ClientBuilder;
     * Client clientOptions = new ClientBuilder().with_network("devnet").build();
     * ```
     * @param network The network we connect to
     */
    fun withNetwork(network: String): ClientBuilder {
        val ret = do_withNetwork(mNativeObj, network)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Set the node sync interval
     * @param node_sync_interval The interval in seconds
     */
    fun withNodeSyncInterval(node_sync_interval: Float): ClientBuilder {
        val ret =
            do_withNodeSyncInterval(mNativeObj, node_sync_interval)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Disables the node syncing process.
     * Every node will be considered healthy and ready to use.
     */
    fun withNodeSyncDisabled(): ClientBuilder {
        val ret = do_withNodeSyncDisabled(mNativeObj)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Set if quorum should be used or not
     * @param quorum `true` if we use a quorum
     */
    fun withQuorum(quorum: Boolean): ClientBuilder {
        val ret = do_withQuorum(mNativeObj, quorum)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Set amount of nodes which should be used for quorum
     * @param quorum_size The amount of nodes
     */
    fun withQuorumSize(quorum_size: Long): ClientBuilder {
        val ret = do_withQuorumSize(mNativeObj, quorum_size)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Set quorum threshold
     * @param threshold The percentage of nodes that need to agree (0-100)
     */
    fun withQuorumThreshold(threshold: Long): ClientBuilder {
        val ret =
            do_withQuorumThreshold(mNativeObj, threshold)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Sets the MQTT broker options.
     * @param options the MQTT options
     */
    fun withMqttBrokerOptions(options: BrokerOptions): ClientBuilder {
        val a0 = options.mNativeObj
        options.mNativeObj = 0
        val ret = do_withMqttBrokerOptions(mNativeObj, a0)
        val convRet = ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
        //TODO  java.lang.ref.Reference.reachabilityFence(options)
        return convRet
    }

    /**
     * Sets whether the PoW should be done locally or remotely.
     * @param local Enables or disables local PoW
     */
    fun withLocalPow(local: Boolean): ClientBuilder {
        val ret = do_withLocalPow(mNativeObj, local)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Sets after how many seconds new tips will be requested during PoW
     * @param tips delay in seconds
     */
    fun withTipsInterval(tips: Long): ClientBuilder {
        val ret = do_withTipsInterval(mNativeObj, tips)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Sets the default request timeout in seconds.
     * @param timeout The request timeout in seconds
     */
    fun withRequestTimeout(timeout: Float): ClientBuilder {
        val ret = do_withRequestTimeout(mNativeObj, timeout)
        return ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Sets the request timeout in seconds for a specific API usage.
     * @param api The API we set the timeout for
     * @param timeout The request timeout in seconds
     */
    fun withApiTimeout(api: Api, timeout: Float): ClientBuilder {
        val a0 = api.value
        val ret = do_withApiTimeout(mNativeObj, a0, timeout)
        val convRet = ClientBuilder(InternalPointerMarker.RAW_PTR, ret)
        //TODO   java.lang.ref.Reference.reachabilityFence(api)
        return convRet
    }

    /**
     * Build the Client instance.
     */
    fun finish(): Client {
        val ret = do_finish(mNativeObj)
        return Client(InternalPointerMarker.RAW_PTR, ret)
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
    var mNativeObj: Long

    companion object {
        private external fun init(): Long
        private external fun do_withNode(self: Long, node: String): Long
        private external fun do_withNodes(self: Long, nodes: Array<String>): Long
        private external fun do_withNodeAuth(
            self: Long,
            node: String,
            jwt: String,
            username: String,
            password: String
        ): Long

        private external fun do_withPrimaryNode(
            self: Long,
            node: String,
            jwt: String,
            username: String,
            password: String
        ): Long

        private external fun do_withPrimaryPowNode(
            self: Long,
            node: String,
            jwt: String,
            username: String,
            password: String
        ): Long

        private external fun do_withPermanode(
            self: Long,
            node: String,
            jwt: String,
            username: String,
            password: String
        ): Long

        private external fun do_withNodePoolUrls(self: Long, node_pool_urls: Array<String>): Long
        private external fun do_withOfflineMode(self: Long): Long
        private external fun do_withNetwork(self: Long, network: String): Long
        private external fun do_withNodeSyncInterval(self: Long, node_sync_interval: Float): Long
        private external fun do_withNodeSyncDisabled(self: Long): Long
        private external fun do_withQuorum(self: Long, quorum: Boolean): Long
        private external fun do_withQuorumSize(self: Long, quorum_size: Long): Long
        private external fun do_withQuorumThreshold(self: Long, threshold: Long): Long
        private external fun do_withMqttBrokerOptions(self: Long, options: Long): Long
        private external fun do_withLocalPow(self: Long, local: Boolean): Long
        private external fun do_withTipsInterval(self: Long, tips: Long): Long
        private external fun do_withRequestTimeout(self: Long, timeout: Float): Long
        private external fun do_withApiTimeout(self: Long, api: Int, timeout: Float): Long
        private external fun do_finish(self: Long): Long
        private external fun do_delete(me: Long)
    }
}