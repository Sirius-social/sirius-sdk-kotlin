package org.iota.client

import kotlin.jvm.Synchronized


/**
 * A builder to build a `Message`.
 */
class MessageBuilder {
    /**
     * Creates a new `MessageBuilder`.
     */
    constructor() {
        mNativeObj = init()
    }

    /**
     * Adds a network id to a `MessageBuilder`.
     * @param network_id The network id
     */
    fun networkId(network_id: Long): MessageBuilder {
        val ret = do_networkId(mNativeObj, network_id)
        return MessageBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Adds parents to a `MessageBuilder`.
     * @param parents A list of parents to set
     */
    fun parents(parents: Array<MessageId>): MessageBuilder {
        val ret = do_parents(mNativeObj, parents)
        return MessageBuilder(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Adds a payload to a `MessageBuilder`.
     * @param payload the MessagePayload to set
     */
    fun payload(payload: org.iota.client.MessagePayload): MessageBuilder {
        val a0: Long = payload.mNativeObj
        payload.mNativeObj = 0
        val ret = do_payload(mNativeObj, a0)
        val convRet = MessageBuilder(InternalPointerMarker.RAW_PTR, ret)
        //TODO  java.lang.ref.Reference.reachabilityFence(payload)
        return convRet
    }

    /**
     * Sets a provider for the nonce. Can currently only be obtained from Client.getPowProvider
     * @param provider Sets the nonce provider
     * @param target_score Target score for the nonce, Recommended: 4000
     */
    fun nonceProvider(provider: ClientMiner, target_score: Double): MessageBuilder {
        val a0 = provider.mNativeObj
        provider.mNativeObj = 0
        val ret = do_nonceProvider(mNativeObj, a0, target_score)
        val convRet = MessageBuilder(InternalPointerMarker.RAW_PTR, ret)
        //TODO    java.lang.ref.Reference.reachabilityFence(provider)
        return convRet
    }

    /**
     * Finish the MessageBuilder
     */
    fun finish(): Message {
        val ret = do_finish(mNativeObj)
        return Message(InternalPointerMarker.RAW_PTR, ret)
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
        private external fun do_networkId(self: Long, network_id: Long): Long
        private external fun do_parents(self: Long, parents: Array<MessageId>): Long
        private external fun do_payload(self: Long, payload: Long): Long
        private external fun do_nonceProvider(
            self: Long,
            provider: Long,
            target_score: Double
        ): Long

        private external fun do_finish(self: Long): Long
        private external fun do_delete(me: Long)
    }
}