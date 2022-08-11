package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Struct containing network and PoW related information
 */
class NetworkInfo {
    override fun toString(): String {
        run { return this.to_string()?:"" }
    }

    override fun equals(obj: Any?): Boolean {
        var equal = false
        if (obj is NetworkInfo) equal = obj.rustEq(this)
        return equal
    }

    override fun hashCode(): Int {
        return mNativeObj.toInt()
    }

    private constructor() {}

    private fun to_string(): String? {
        return do_to_string(mNativeObj)
    }

    private fun rustEq(o: NetworkInfo): Boolean {
        val a0 = o.mNativeObj
        val ret = do_rustEq(mNativeObj, a0)
    // TODO    java.lang.ref.Reference.reachabilityFence(o)
        return ret
    }

    fun network(): String? {
        val ret = do_network(mNativeObj)
        return ret
    }

    fun networkId(): Long {
        return do_networkId(mNativeObj)
    }

    fun bech32Hrp(): String? {
        return do_bech32Hrp(mNativeObj)
    }

    fun minPowScore(): Double {
        return do_minPowScore(mNativeObj)
    }

    fun localPow(): Boolean {
        return do_localPow(mNativeObj)
    }

    fun fallbackToLocalPow(): Boolean {
        return do_fallbackToLocalPow(mNativeObj)
    }

    fun tipsInterval(): Long {
        return do_tipsInterval(mNativeObj)
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
        private external fun do_to_string(self: Long): String?
        private external fun do_rustEq(self: Long, o: Long): Boolean
        private external fun do_network(self: Long): String?
        private external fun do_networkId(self: Long): Long
        private external fun do_bech32Hrp(self: Long): String?
        private external fun do_minPowScore(self: Long): Double
        private external fun do_localPow(self: Long): Boolean
        private external fun do_fallbackToLocalPow(self: Long): Boolean
        private external fun do_tipsInterval(self: Long): Long
        private external fun do_delete(me: Long)
    }
}