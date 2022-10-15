package com.sirius.library.utils.multibase

import com.sirius.library.utils.StringUtils

object Multibase {
    //Long here is BigInteger
    enum class Base(val prefix: Char, val alphabet: String) {
        BASE2('0', "01"),
        BASE8('7', "01234567"),
        BASE10('9', "0123456789"),
        BASE16('f', "0123456789abcdef"),
        BASE16_UPPER('F', "0123456789ABCDEF"),
        BASE32('b', "abcdefghijklmnopqrstuvwxyz234567"),
        BASE32_UPPER('B', "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"),
        BASE32_PAD('c', "abcdefghijklmnopqrstuvwxyz234567="),
        BASE32_PAD_UPPER('C', "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567="),
        BASE32_HEX('v', "0123456789abcdefghijklmnopqrstuvw"),
        BASE32_HEX_UPPER('V', "0123456789ABCDEFGHIJKLMNOPQRSTUVW"),
        BASE32_HEX_PAD('t', "0123456789abcdefghijklmnopqrstuvw="),
        BASE32_HEX_PAD_UPPER('T', "0123456789ABCDEFGHIJKLMNOPQRSTUVW="),
        BASE58_FLICKR('Z', "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ"),
        BASE58_BTC('z', "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"),
        BASE64('m', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"),
        BASE64_URL('u', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"),
        BASE64_PAD('M', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="),
        BASE64_URL_PAD('U', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_=");

        companion object {

            private val baseMap = HashMap<Char, Base>()

            init {
                for (base in values()) {
                    baseMap[base.prefix] = base
                }
            }

            fun lookup(prefix: Char): Base {
                return baseMap[prefix]
                    ?: throw IllegalStateException("Unknown Multibase type: $prefix")
            }
        }
    }


    fun encode(base: Base, data: ByteArray): String {
        return when (base) {
//            BASE2 -> base.prefix + String(BinaryCodec().encode(data))
//            BASE8 -> base.prefix + BaseN.encode(base.alphabet, BigInteger("8"), data)
//            BASE10 -> base.prefix + BaseN.encode(base.alphabet, BigInteger("10"), data)
            Base.BASE16 -> throw NotImplementedError()//base.prefix + BaseN.encode(base.alphabet, 16, data)
            Base.BASE16_UPPER -> throw NotImplementedError()//base.prefix + BaseN.encode(base.alphabet, 16, data)
            Base.BASE32 -> throw NotImplementedError()//base.prefix + BaseN.encode(base.alphabet, 32, data)
            Base.BASE32_UPPER -> throw NotImplementedError()//base.prefix + BaseN.encode(base.alphabet, 32, data)
            Base.BASE32_PAD -> throw NotImplementedError()//base.prefix + Base32().encodeToString(data).toLowerCase()
            Base.BASE32_PAD_UPPER -> throw NotImplementedError()//base.prefix + BaseN.encode(base.alphabet, 32, data)
            Base.BASE32_HEX -> throw NotImplementedError()//base.prefix + BaseN.encode(base.alphabet, 32, data)
            Base. BASE32_HEX_UPPER -> throw NotImplementedError()//base.prefix + BaseN.encode(base.alphabet, 32, data)
            Base.BASE32_HEX_PAD -> throw NotImplementedError()//base.prefix + Base32(true).encodeToString(data).toLowerCase()
            Base. BASE32_HEX_PAD_UPPER -> throw NotImplementedError()//base.prefix + Base32(true).encodeToString(data)
            Base.BASE58_FLICKR -> base.prefix + Base58.encode( data) //BaseN.encode(base.alphabet, 58, data)
            Base.BASE58_BTC -> base.prefix + Base58.encode( data) //BaseN.encode(base.alphabet, 58, data)
            Base.BASE64 -> base.prefix + StringUtils.bytesToString(Base64.getEncoder().encode(data),StringUtils.CODEC.UTF_8)//BaseN.encode(base.alphabet, 64, data)
            Base. BASE64_URL -> base.prefix + StringUtils.bytesToString(Base64.getUrlEncoder().encode(data),StringUtils.CODEC.UTF_8)//BaseN.encode(base.alphabet,64, data)
            Base. BASE64_PAD -> base.prefix + StringUtils.bytesToString(Base64.getEncoder().encode(data), StringUtils.CODEC.UTF_8)
            Base. BASE64_URL_PAD -> base.prefix + StringUtils.bytesToString(Base64.getUrlEncoder().encode(data),StringUtils.CODEC.UTF_8)
            else -> throw IllegalStateException("UnImplement multi type")
        }
    }

    fun decode(data: String): ByteArray {
        val prefix = data[0]
        val rest = data.substring(1)
        val base = Base.lookup(prefix)
        return when (base) {
//            BASE2 -> BinaryCodec().decode(rest.toByteArray())
//            BASE8 -> BaseN.decode(base.alphabet, BigInteger("8"), rest)
//            BASE10 -> BaseN.decode(base.alphabet, BigInteger("10"), rest)
            Base. BASE16 -> throw NotImplementedError()//BaseN.decode(base.alphabet, BigInteger("16"), rest)
            Base. BASE16_UPPER -> throw NotImplementedError()//BaseN.decode(base.alphabet, BigInteger("16"), rest)
            Base. BASE32 -> throw NotImplementedError()//BaseN.decode(base.alphabet, BigInteger("32"), rest)
            Base. BASE32_UPPER -> throw NotImplementedError()//BaseN.decode(base.alphabet, BigInteger("32"), rest)
            Base.BASE32_PAD -> throw NotImplementedError()//Base32().decode(rest)
            Base. BASE32_PAD_UPPER -> throw NotImplementedError()//BaseN.decode(base.alphabet, BigInteger("32"), rest)
            Base. BASE32_HEX -> throw NotImplementedError()//BaseN.decode(base.alphabet, BigInteger("32"), rest)
            Base. BASE32_HEX_UPPER -> throw NotImplementedError()//BaseN.decode(base.alphabet, BigInteger("32"), rest)
            Base. BASE32_HEX_PAD -> throw NotImplementedError()//Base32(true).decode(rest)
            Base.BASE32_HEX_PAD_UPPER -> throw NotImplementedError()//Base32(true).decode(rest)
            Base. BASE58_FLICKR ->  Base58.decode(rest)//BaseN.decode(base.alphabet, BigInteger("58"), rest)
            Base. BASE58_BTC -> Base58.decode(rest)//BaseN.decode(base.alphabet, BigInteger("58"), rest)
            Base. BASE64 -> Base64.getDecoder().decode(rest)// BaseN.decode(base.alphabet, BigInteger("64"), rest)
            Base. BASE64_URL -> Base64.getUrlDecoder().decode(rest) // BaseN.decode(base.alphabet, BigInteger("64"), rest)
            Base.  BASE64_PAD -> Base64.getDecoder().decode(rest)
            Base.  BASE64_URL_PAD -> Base64.getUrlDecoder().decode(rest)
            else -> throw IllegalStateException("UnImplement multi type")
        }
    }
}