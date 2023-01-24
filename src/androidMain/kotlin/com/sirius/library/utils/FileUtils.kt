package com.sirius.library.utils

import com.sirius.library.mobile.helpers.WalletHelper
import io.ktor.http.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

actual class FileUtils {
    actual companion object {
        actual fun forceDelete(dirPath: String, subDir: String?) {
            try{
                var path = dirPath
                if (subDir != null) {
                    path = dirPath + File.separator + subDir
                }
                val file = File(path)
                deleteDirectory(file)

            }catch (e : Exception){
                e.printStackTrace()
            }

        }

        @Throws(IOException::class)
        fun deleteDirectory(directory: File) {
            if (directory.exists()) {
                if (!isSymlink(directory)) {
                    cleanDirectory(directory)
                }
                if (!directory.delete()) {
                    val message = "Unable to delete directory $directory."
                    throw IOException(message)
                }
            }
        }

        fun isSystemWindows(): Boolean {
            val SYSTEM_SEPARATOR = File.separatorChar
            return SYSTEM_SEPARATOR == '\\'
        }

        @Throws(IOException::class)
        fun isSymlink(file: File?): Boolean {
            return if (file == null) {
                throw NullPointerException("File must not be null")
            } else if (FileUtils.isSystemWindows()) {
                false
            } else {
                var fileInCanonicalDir: File? = null
                fileInCanonicalDir = if (file.parent == null) {
                    file
                } else {
                    val canonicalDir = file.parentFile.canonicalFile
                    File(canonicalDir, file.name)
                }
                fileInCanonicalDir.canonicalFile != fileInCanonicalDir.absoluteFile
            }
        }

        @Throws(IOException::class)
        fun cleanDirectory(directory: File) {
            val message: String
            if (!directory.exists()) {
                message = "$directory does not exist"
                throw IllegalArgumentException(message)
            } else if (!directory.isDirectory) {
                message = "$directory is not a directory"
                throw IllegalArgumentException(message)
            } else {
                val files = directory.listFiles()
                if (files == null) {
                    throw IOException("Failed to list contents of $directory")
                } else {
                    var exception: IOException? = null
                    val `len$` = files.size
                    for (`i$` in 0 until `len$`) {
                        val file = files[`i$`]
                        try {
                            FileUtils.forceDelete(file)
                        } catch (var8: IOException) {
                            exception = var8
                        }
                    }
                    if (null != exception) {
                        throw exception
                    }
                }
            }
        }

        fun forceDeleteWithoutException(url: String?) {
            try {
                FileUtils.forceDelete(File(url))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun forceDeleteWithoutException(file: File) {
            try {
                FileUtils.forceDelete(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @Throws(IOException::class)
        fun forceDelete(file: File) {
            if (file.isDirectory) {
                FileUtils.deleteDirectory(file)
            } else {
                val filePresent = file.exists()
                if (!file.delete()) {
                    if (!filePresent) {
                        throw FileNotFoundException("File does not exist: $file")
                    }
                    val message = "Unable to delete file: $file"
                    throw IOException(message)
                }
            }
        }


    }


}