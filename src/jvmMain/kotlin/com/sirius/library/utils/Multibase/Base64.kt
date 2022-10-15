package com.sirius.library.utils.multibase

actual class Base64 {
    actual companion object {
        actual fun getUrlDecoder(): Base64.Decoder {
            return Decoder(true)
        }

        actual fun getDecoder(): Base64.Decoder {
            return Decoder(false)
        }

        actual fun getUrlEncoder(): Base64.Encoder {
            return Base64.Encoder(true)
        }

        actual fun getEncoder(): Base64.Encoder {
            return Base64.Encoder(false)
        }

    }

    actual class Decoder actual constructor(var isUrl : Boolean) {

        actual fun decode(byteArray: ByteArray): ByteArray {
            if(isUrl){
                return java.util.Base64.getUrlDecoder().decode(byteArray);
                //  return android.util.Base64.decode(byteArray, android.util.Base64.URL_SAFE  or android.util.Base64.NO_WRAP)
            }
            return java.util.Base64.getDecoder().decode(byteArray);
            // return android.util.Base64.decode(byteArray, android.util.Base64.NO_WRAP)
        }

        actual fun decode(string: String): ByteArray {
            if(isUrl){
                return java.util.Base64.getUrlDecoder().decode(string);
                //  return android.util.Base64.decode(string, android.util.Base64.URL_SAFE  or android.util.Base64.NO_WRAP)
            }
            return java.util.Base64.getDecoder().decode(string);
            //   return android.util.Base64.decode(string, android.util.Base64.NO_WRAP)
        }


    }

    actual class Encoder actual constructor(var isUrl : Boolean) {
        actual fun encode(byteArray: ByteArray?): ByteArray {
            if(isUrl){
                return java.util.Base64.getUrlEncoder().encode(byteArray);
                // return android.util.Base64.encode(byteArray, android.util.Base64.URL_SAFE  or android.util.Base64.NO_WRAP)
            }
            return java.util.Base64.getEncoder().encode(byteArray);
            //   return android.util.Base64.encode(byteArray, android.util.Base64.NO_WRAP)
        }
    }
}