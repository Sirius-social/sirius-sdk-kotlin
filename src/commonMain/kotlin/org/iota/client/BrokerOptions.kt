package org.iota.client

import kotlin.jvm.Synchronized

/**
 * The MQTT broker options.
 */
class BrokerOptions {
    constructor() {
        mNativeObj = init()
    }

    /**
     * Whether the MQTT broker should be automatically disconnected when all topics are unsubscribed or not.
     * @param disconnect
     */
    fun automaticDisconnect(disconnect: Boolean): BrokerOptions {
        val ret =
            do_automaticDisconnect(mNativeObj, disconnect)
        return BrokerOptions(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * timeout of the mqtt broker.
     * @param timeout The timeout in seconds
     */
    fun timeout(timeout: Float): BrokerOptions {
        val ret = do_timeout(mNativeObj, timeout)
        return BrokerOptions(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Defines if websockets should be used (true) or TCP (false)
     * @param use_ws If we use web sockets or not
     */
    fun useWs(use_ws: Boolean): BrokerOptions {
        val ret = do_useWs(mNativeObj, use_ws)
        return BrokerOptions(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Defines the port to be used for the MQTT connection
     * @param port The port we use to conenct
     */
    fun port(port: Int): BrokerOptions {
        val ret = do_port(mNativeObj, port)
        return BrokerOptions(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Defines the maximum reconnection attempts before it returns an error
     * @param max_reconnection_attempts The maximum attempts
     */
    fun maxReconnectionAttempts(max_reconnection_attempts: Long): BrokerOptions {
        val ret = do_maxReconnectionAttempts(
            mNativeObj,
            max_reconnection_attempts
        )
        return BrokerOptions(org.iota.client.InternalPointerMarker.RAW_PTR, ret)
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
        check(marker == InternalPointerMarker.RAW_PTR)
        mNativeObj = ptr
    }

    /*package*/
    var mNativeObj: Long

    companion object {
        private external fun init(): Long
        private external fun do_automaticDisconnect(self: Long, disconnect: Boolean): Long
        private external fun do_timeout(self: Long, timeout: Float): Long
        private external fun do_useWs(self: Long, use_ws: Boolean): Long
        private external fun do_port(self: Long, port: Int): Long
        private external fun do_maxReconnectionAttempts(
            self: Long,
            max_reconnection_attempts: Long
        ): Long

        private external fun do_delete(me: Long)
    }
}