package org.iota.client

import kotlin.jvm.Synchronized


class GetMessageBuilder {
    private constructor() {}

    /**
     * GET /api/v1/messages?index={Index} endpoint
     * Consume the builder and search for messages matching the index
     * @param index the index string
     */
    fun indexString(index: String): Array<org.iota.client.MessageId> {
        return do_indexString(mNativeObj, index)
    }

    /**
     * GET /api/v1/messages?index={Index} endpoint
     * Consume the builder and search for messages matching the index
     * @param index the index in bytes
     */
    fun indexVec(index: ByteArray): Array<org.iota.client.MessageId> {
        return do_indexVec(mNativeObj, index)
    }

    /**
     * GET /api/v1/messages/{messageID} endpoint
     * Consume the builder and find a message by its identifer. This method returns the given message object.
     * @param message_id The id of the message to find
     */
    fun data(message_id: org.iota.client.MessageId): org.iota.client.Message {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret = do_data(mNativeObj, a0)
        val convRet: org.iota.client.Message =
            org.iota.client.Message(InternalPointerMarker.RAW_PTR, ret)
        //TODO    java.lang.ref.Reference.reachabilityFence(message_id)
        return convRet
    }

    /**
     * GET /api/v1/messages/{messageID}/metadata endpoint
     * Consume the builder and find a message by its identifer. This method returns the given message metadata.
     * @param message_id The id of the message to find
     */
    fun metadata(message_id: org.iota.client.MessageId): MessageMetadata {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret = do_metadata(mNativeObj, a0)
        val convRet = MessageMetadata(InternalPointerMarker.RAW_PTR, ret)
        //TODO  java.lang.ref.Reference.reachabilityFence(message_id)
        return convRet
    }

    /**
     * GET /api/v1/messages/{messageID}/raw endpoint
     * Consume the builder and find a message by its identifer. This method returns the given message raw data.
     * @param message_id The id of the message to find
     */
    fun raw(message_id: org.iota.client.MessageId): String {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret = do_raw(mNativeObj, a0)
        //TODO  java.lang.ref.Reference.reachabilityFence(message_id)
        return ret
    }

    /**
     * GET /api/v1/messages/{messageID}/children endpoint
     * Consume the builder and returns the list of message IDs that reference a message by its identifier.
     * @param message_id The id of the message to find
     */
    fun children(message_id: org.iota.client.MessageId): Array<org.iota.client.MessageId> {
        val a0: Long = message_id.mNativeObj
        message_id.mNativeObj = 0
        val ret: Array<org.iota.client.MessageId> = do_children(mNativeObj, a0)
        //TODO  java.lang.ref.Reference.reachabilityFence(message_id)
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
        private external fun do_indexString(
            self: Long,
            index: String
        ): Array<org.iota.client.MessageId>

        private external fun do_indexVec(
            self: Long,
            index: ByteArray
        ): Array<org.iota.client.MessageId>

        private external fun do_data(self: Long, message_id: Long): Long
        private external fun do_metadata(self: Long, message_id: Long): Long
        private external fun do_raw(self: Long, message_id: Long): String
        private external fun do_children(
            self: Long,
            message_id: Long
        ): Array<org.iota.client.MessageId>

        private external fun do_delete(me: Long)
    }
}