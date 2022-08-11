package org.iota.client

import kotlin.jvm.Synchronized


/**
 * Describes a peer.
 */
class PeerDto {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The id of the peer
     */
    fun id(): String {
        return do_id(mNativeObj)
    }

    /**
     * multi addresses
     */
    fun multiAddresses(): Array<String> {
        return do_multiAddresses(mNativeObj)
    }

    /**
     * The alias of the peer
     */
    fun alias(): String? {
        val ret = do_alias(mNativeObj)
        return ret
    }

    /**
     * The type of peer
     */
    fun relation(): Relation {
        val ret = do_relation(mNativeObj)
        return Relation.fromInt(ret)
    }

    /**
     * `true` if this peer is connected
     */
    fun connected(): Boolean {
        return do_connected(mNativeObj)
    }

    /**
     * The gossip information of this peer
     */
    fun gossip(): GossipDto? {
        val ret = do_gossip(mNativeObj)
        val convRet: GossipDto? = if (ret != 0L) {
                GossipDto(
                    InternalPointerMarker.RAW_PTR,
                    ret
                )

        } else {
            null
        }
        return convRet
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
        private external fun do_to_string(self: Long): String
        private external fun do_id(self: Long): String
        private external fun do_multiAddresses(self: Long): Array<String>
        private external fun do_alias(self: Long): String?
        private external fun do_relation(self: Long): Int
        private external fun do_connected(self: Long): Boolean
        private external fun do_gossip(self: Long): Long
        private external fun do_delete(me: Long)
    }
}