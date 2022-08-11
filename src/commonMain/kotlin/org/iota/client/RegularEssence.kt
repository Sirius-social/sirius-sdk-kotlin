package org.iota.client

import kotlin.jvm.Synchronized


class RegularEssence {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Gets the transaction inputs.
     */
    fun inputs(): Array<Input> {
        return do_inputs(mNativeObj)
    }

    /**
     * Gets the transaction outputs.
     */
    fun outputs(): Array<org.iota.client.Output> {
        return do_outputs(mNativeObj)
    }

    fun payload(): MessagePayload? {
        val ret = do_payload(mNativeObj)
        val convRet: MessagePayload? = if (ret != 0L) {
                org.iota.client.MessagePayload(
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
        private external fun do_inputs(self: Long): Array<Input>
        private external fun do_outputs(self: Long): Array<org.iota.client.Output>
        private external fun do_payload(self: Long): Long
        private external fun do_delete(me: Long)
    }
}