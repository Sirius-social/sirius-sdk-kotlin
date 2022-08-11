package org.iota.client.local

import com.sirius.library.utils.System

/**
 * The NativeAPI class, which houses all entrypoints to the shared library.
 */
object NativeAPI {
    // Stores any errors that were encountered at library load time
    private var INIT_ERROR: Throwable? = null
    fun verifyLink() {
        checkAvailability()
        verify_link()
    }

    private external fun verify_link(): Int

    /**
     * Checks whether the library was loaded successfully before calling into a
     * given function, for cleaner exception messages.
     */
    fun checkAvailability() {
        if (INIT_ERROR != null) {
            throw RuntimeException(INIT_ERROR)
        }
    }

    // The static block below loads the iota_client library. It will be
    // executed the first time the NativeAPI is used. Later, it will contain
    // other initialization logic.
    init {
        var error: Throwable? = null
        try {
           System.loadLibrary("iota_client")
        } catch (t: Throwable) {
            error = t
        }
        INIT_ERROR = error
    }
}
