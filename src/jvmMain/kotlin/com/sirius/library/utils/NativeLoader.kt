package com.sirius.library.utils

actual object NativeLoader {
    actual fun loadNative(name: String)  {
        try {
            //"iota_client"
             System.loadLibrary(name)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}