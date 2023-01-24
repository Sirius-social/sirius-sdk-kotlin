package com.sirius.library.utils

expect class FileUtils {
    companion object {
        fun forceDelete(dirPath: String, subDir: String?)
    }


}