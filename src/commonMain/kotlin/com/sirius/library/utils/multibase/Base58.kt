package com.sirius.library.utils.multibase

import com.sirius.library.utils.StringUtils

/**
 *
 * Base58 is a way to encode Bitcoin addresses as numbers and letters. Note that this is not the same base58 as used by
 * Flickr, which you may see reference to around the internet.
 *
 *
 * You may instead wish to work with [], which adds support for testing the prefix
 * and suffix bytes commonly found in addresses.
 *
 *
 * Satoshi says: why base-58 instead of standard base-64 encoding?
 *
 *
 *
 *
 *  * Don't want 0OIl characters that look the same in some fonts and
 * could be used to create visually identical looking account numbers.
 *  * A string with non-alphanumeric characters is not as easily accepted as an account number.
 *  * E-mail usually won't line-break if there's no punctuation to break at.
 *  * Doubleclicking selects the whole number as one word if it's all alphanumeric.
 *
 */
object Base58 {
    val ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray()
    private val INDEXES = IntArray(128)

    /**
     * Encodes the given bytes in base58. No checksum is appended.
     */
    fun encode(input: ByteArray): String {
        var input = input
        if (input.size == 0) {
            return ""
        }
        input = copyOfRange(input, 0, input.size)
        // Count leading zeroes.
        var zeroCount = 0
        while (zeroCount < input.size && input[zeroCount] == 0.toByte()) {
            ++zeroCount
        }
        // The actual encoding.
        val temp = ByteArray(input.size * 2)
        var j = temp.size
        var startAt = zeroCount
        while (startAt < input.size) {
            val mod = divmod58(input, startAt)
            if (input[startAt] == 0.toByte()) {
                ++startAt
            }
            temp[--j] = ALPHABET[mod.toInt()].toByte()
        }

        // Strip extra '1' if there are some after decoding.
        while (j < temp.size && temp[j] == ALPHABET[0].toByte()) {
            ++j
        }
        // Add as many leading '1' as there were leading zeros.
        while (--zeroCount >= 0) {
            temp[--j] = ALPHABET[0].toByte()
        }
        val output = copyOfRange(temp, j, temp.size)
        var string = StringUtils.bytesToString(output, StringUtils.CODEC.US_ASCII)
        if (string.length > 22 && string.startsWith("1")) {
            string = string.substring(1)
        }
        return string
    }


    fun decode(input: String): ByteArray {
        if (input.length == 0) {
            return ByteArray(0)
        }
        val input58 = ByteArray(input.length)
        // Transform the String to a base58 byte sequence
        for (i in 0 until input.length) {
            val c = input[i]
            var digit58 = -1
            if (c.toInt() >= 0 && c.toInt() < 128) {
                digit58 = INDEXES[c.toInt()]
            }
            if (digit58 < 0) {
                throw IllegalArgumentException("Illegal character $c at $i")
            }
            input58[i] = digit58.toByte()
        }
        // Count leading zeroes
        var zeroCount = 0
        while (zeroCount < input58.size && input58[zeroCount] == 0.toByte()) {
            ++zeroCount
        }
        // The encoding
        val temp = ByteArray(input.length)
        var j = temp.size
        var startAt = zeroCount
        while (startAt < input58.size) {
            val mod = divmod256(input58, startAt)
            if (input58[startAt] == 0.toByte()) {
                ++startAt
            }
            temp[--j] = mod
        }
        // Do no add extra leading zeroes, move j to first non null byte.
        while (j < temp.size && temp[j] == 0.toByte()) {
            ++j
        }
        return copyOfRange(temp, j - zeroCount, temp.size)
    }


    @Throws(Exception::class)
    fun decodeToBigInteger(input: String): Int {
        return 0
    }

    /**
     * Uses the checksum in the last 4 bytes of the decoded data to verify the rest are correct. The checksum is
     * removed from the returned data.
     *
     * @throws if the input is not base 58 or the checksum does not validate.
     */
    /*    public static byte[] decodeChecked(String input) throws IllegalArgumentException {
        byte tmp [] = decode(input);
        if (tmp.length < 4)
            throw new IllegalArgumentException("Input too short");
        byte[] bytes = copyOfRange(tmp, 0, tmp.length - 4);
        byte[] checksum = copyOfRange(tmp, tmp.length - 4, tmp.length);

        tmp = UtilsBBase.doubleDigest(bytes);
        byte[] hash = copyOfRange(tmp, 0, 4);
        if (!Arrays.equals(checksum, hash))
            throw new IllegalArgumentException("Checksum does not validate");

        return bytes;
    }*/
    //
    // number -> number / 58, returns number % 58
    //
    private fun divmod58(number: ByteArray, startAt: Int): Byte {
        var remainder = 0
        for (i in startAt until number.size) {
            val digit256 = number[i].toInt() and 0xFF
            val temp = remainder * 256 + digit256
            number[i] = (temp / 58).toByte()
            remainder = temp % 58
        }
        return remainder.toByte()
    }

    //
    // number -> number / 256, returns number % 256
    //
    private fun divmod256(number58: ByteArray, startAt: Int): Byte {
        var remainder = 0
        for (i in startAt until number58.size) {
            val digit58 = number58[i].toInt() and 0xFF
            val temp = remainder * 58 + digit58
            number58[i] = (temp / 256).toByte()
            remainder = temp % 256
        }
        return remainder.toByte()
    }

    private fun copyOfRange(source: ByteArray, from: Int, to: Int): ByteArray {
        //val range = ByteArray(to - from)
        return source.copyOfRange(from,to)

      //  java.lang.System.arraycopy(source, from, range, 0, range.size)
        //return range
    }

    init {
        for (i in INDEXES.indices) {
            INDEXES[i] = -1
        }
        for (i in ALPHABET.indices) {
            INDEXES[ALPHABET[i].toInt()] = i
        }
    }
}