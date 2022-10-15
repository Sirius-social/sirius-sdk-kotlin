package com.sirius.library.utils

actual object NativeLoader {
    actual fun loadNative(name: String)  {
        try {
            System.loadLibrary(name)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}