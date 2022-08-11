package org.iota.client

import kotlin.jvm.Synchronized


class MqttTopicManager {
    private constructor() {}

    /**
     * Add a new topic to the list.
     */
    fun withTopic(topic: Topic): MqttTopicManager {
        val a0 = topic.mNativeObj
        topic.mNativeObj = 0
        val ret = do_withTopic(mNativeObj, a0)
        val convRet = MqttTopicManager(InternalPointerMarker.RAW_PTR, ret)
        //TODO     java.lang.ref.Reference.reachabilityFence(topic)
        return convRet
    }

    /**
     * Add a collection of topics to the list.
     */
    fun withTopics(topics: Array<Topic>): MqttTopicManager {
        val ret = do_withTopics(mNativeObj, topics)
        return MqttTopicManager(InternalPointerMarker.RAW_PTR, ret)
    }

    /**
     * Unsubscribe from the given topics.
     * If no topics were provided, the function will unsubscribe from every subscribed topic.
     */
    fun unsubscribe() {
        do_unsubscribe(mNativeObj)
    }

    fun subscribe(cb: org.iota.client.MqttListener) {
        do_subscribe(mNativeObj, cb)
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
          //  super.finalize()
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
        private external fun do_withTopic(self: Long, topic: Long): Long
        private external fun do_withTopics(self: Long, topics: Array<Topic>): Long
        private external fun do_unsubscribe(self: Long)
        private external fun do_subscribe(self: Long, cb: org.iota.client.MqttListener)
        private external fun do_delete(me: Long)
    }
}