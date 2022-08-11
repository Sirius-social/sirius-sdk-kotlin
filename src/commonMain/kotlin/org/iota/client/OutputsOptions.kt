package org.iota.client

import kotlin.jvm.Synchronized


class OutputsOptions {
    override fun toString(): String {
        run { return this.to_string() }
    }

    /**
     * Creates a new instance of output options with default values
     */
    constructor() {
        mNativeObj = init()
    }

    private fun to_string(): String {
        return do_to_string(mNativeObj)
    }

    /**
     * Whether the query should include spent outputs or not.
     */
    fun includeSpent(include_spent: Boolean) {
        do_includeSpent(mNativeObj, include_spent)
    }

    /**
     * The output type filter.
     */
    fun outputType(output_type: org.iota.client.OutputKind?) {
        val a0 = output_type?.value ?: -1
        do_outputType(mNativeObj, a0)
        //TODO  java.lang.ref.Reference.reachabilityFence(output_type)
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
           // super.finalize()
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
        private external fun do_to_string(self: Long): String
        private external fun do_includeSpent(self: Long, include_spent: Boolean)
        private external fun do_outputType(self: Long, output_type: Int)
        private external fun do_delete(me: Long)
    }
}