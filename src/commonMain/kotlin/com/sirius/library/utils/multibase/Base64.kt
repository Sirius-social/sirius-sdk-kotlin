package com.sirius.library.utils.multibase

expect class Base64 {

    companion object{
        fun getUrlDecoder() : Decoder

        fun getDecoder() : Decoder

        fun getUrlEncoder() : Encoder

        fun getEncoder() : Encoder
    }

    class Decoder(isUrl : Boolean){

        fun decode(byteArray : ByteArray) : ByteArray

        fun decode(string: String) : ByteArray
    }

     class Encoder(isUrl : Boolean){
         fun encode(byteArray : ByteArray?) : ByteArray
    }

}