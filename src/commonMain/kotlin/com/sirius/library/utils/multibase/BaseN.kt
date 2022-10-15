package com.sirius.library.utils.multibase

import io.ktor.utils.io.core.*

object BaseN {
    fun decode(alphabet: String, base: Long, input: String): ByteArray {
        return input.toByteArray()
    }
    fun encode(alphabet: String, base: Long, input: ByteArray): String {
        return input.toString()
    }

   /* fun decode(alphabet: String, base: BigInteger, input: String): ByteArray {
        val bytes = decodeToBigInteger(alphabet, base, input).toByteArray()
        val stripSignByte = bytes.size > 1 && bytes[0].compareTo(0) == 0 && bytes[1] < 0
        var leadingZeros = 0
        var i = 0
        while (input[i] == alphabet[0]) {
            leadingZeros++
            i++
        }
        val tmp = ByteArray(bytes.size - (if (stripSignByte) 1 else 0) + leadingZeros)
        System.arraycopy(bytes, if (stripSignByte) 1 else 0, tmp, leadingZeros, tmp.size - leadingZeros)
        return tmp
    }

    fun encode(alphabet: String, base: BigInteger, input: ByteArray): String {
        var bi = BigInteger(1, input)
        val sb = StringBuffer()
        while (bi >= base) {
            //求余
            val mod = bi.mod(base)
            sb.insert(0, alphabet[mod.toInt()])
            bi = bi.subtract(mod).divide(base)
        }
        sb.insert(0, alphabet[bi.toInt()])
        //convert leading zeros.
        for (b in input) {
            if (b.compareTo(0) == 0) {
                sb.insert(0, alphabet[0])
            } else {
                break
            }
        }
        return sb.toString()
    }

    fun decodeToBigInteger(alphabet: String, base: BigInteger, input: String): BigInteger {
        var bi = BigInteger.valueOf(0)
        for (i in input.length - 1 downTo 0) {
            val alphaIndex = alphabet.indexOf(input[i])
            if (alphaIndex == -1) {
                throw IllegalStateException("Illegal character " + input[i] + " at " + i)
            }
            bi = bi.add(BigInteger.valueOf(alphaIndex.toLong()).multiply(base.pow(input.length - 1 - i)))
        }
        return bi
    }*/
}