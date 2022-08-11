package org.iota.client

import kotlin.jvm.Synchronized

/**
 * Response of GET /api/v1/outputs/{output_id}.
 * Returns all information about a specific output.
 */
class OutputResponse {
    override fun toString(): String {
        run { return this.to_string() }
    }

    private constructor() {}

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * The message id this output is related to
     */
    fun messageId(): String {
        return do_messageId(mNativeObj)
    }

    /**
     * The transaction id this output is related to
     */
    fun transactionId(): String {
        return do_transactionId(mNativeObj)
    }

    /**
     * The output index
     */
    fun outputIndex(): Int {
        return do_outputIndex(mNativeObj)
    }

    /**
     * `true` if this output was spent. Otherwise `false`
     */
    val isSpent: Boolean
        get() = do_isSpent(mNativeObj)

    /**
     * Get the output object which can be turned into its specific output type
     */
    fun output(): org.iota.client.OutputDto {
        val ret = do_output(mNativeObj)
        return org.iota.client.OutputDto(InternalPointerMarker.RAW_PTR, ret)
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
      //      super.finalize()
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
        private external fun do_messageId(self: Long): String
        private external fun do_transactionId(self: Long): String
        private external fun do_outputIndex(self: Long): Int
        private external fun do_isSpent(self: Long): Boolean
        private external fun do_output(self: Long): Long
        private external fun do_delete(me: Long)
    }
}