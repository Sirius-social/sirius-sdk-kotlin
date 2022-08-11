package org.iota.client

import kotlin.jvm.Synchronized


class TreasuryPayload {
    override fun toString(): String {
        run { return this.to_string() }
    }

    constructor(input: TreasuryInput, output: org.iota.client.TreasuryOutput) {
        val a0 = input.mNativeObj
        input.mNativeObj = 0
        val a1: Long = output.mNativeObj
        output.mNativeObj = 0
        mNativeObj = init(a0, a1)
        //TODO  java.lang.ref.Reference.reachabilityFence(input)
        //TODO   java.lang.ref.Reference.reachabilityFence(output)
    }

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Serializes the treasury payload into a json string
     */
    fun serialize(): String {
        return do_serialize(mNativeObj)
    }

    fun output(): org.iota.client.TreasuryOutput {
        val ret = do_output(mNativeObj)
        return org.iota.client.TreasuryOutput(InternalPointerMarker.RAW_PTR, ret)
    }

    fun input(): TreasuryInput {
        val ret = do_input(mNativeObj)
        return TreasuryInput(InternalPointerMarker.RAW_PTR, ret)
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
        private external fun init(input: Long, output: Long): Long
        private external fun do_to_string(self: Long): String
        private external fun do_serialize(self: Long): String

        /**
         * Turns a serialized treasury payload string back into its class
         */
        fun deserialize(serialised_data: String): TreasuryPayload {
            val ret = do_deserialize(serialised_data)
            return TreasuryPayload(InternalPointerMarker.RAW_PTR, ret)
        }

        private external fun do_deserialize(serialised_data: String): Long
        private external fun do_output(self: Long): Long
        private external fun do_input(self: Long): Long
        private external fun do_delete(me: Long)
    }
}