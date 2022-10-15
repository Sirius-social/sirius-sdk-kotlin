package com.sirius.library.encryption

import com.ionspin.kotlin.crypto.secretbox.crypto_secretbox_KEYBYTES
import com.ionspin.kotlin.crypto.signature.Signature
import com.ionspin.kotlin.crypto.signature.crypto_sign_BYTES
import com.sirius.library.errors.sirius_exceptions.SiriusCryptoError
import com.sirius.library.utils.*
import com.sirius.library.utils.multibase.Base58
import com.sirius.library.utils.multibase.Base64
import com.sodium.LibSodium
import com.sodium.SodiumException

object Custom {
    /**
     * Convert a base 64 string to bytes.
     *
     * @param value   input base64 value
     * @param urlSafe flag if needed to convert to urlsafe presentation
     * @return bytes array
     */
    fun b64ToBytes(value: String, urlSafe: Boolean): ByteArray {
        var valueBytes: ByteArray = StringUtils.stringToBytes(value, StringUtils.CODEC.US_ASCII)
        /*   if isinstance(value, str):
        value = value.encode('ascii')
        if urlsafe:
        missing_padding = len(value) % 4
        if missing_padding:
        value += b'=' * (4 - missing_padding)
        return base64.urlsafe_b64decode(value)
        return base64.b64decode(value)
*/

        val encodedByte : ByteArray = if (urlSafe) {
        //    val missing_padding  = valueBytes.size % 4
         //   println("missing_padding="+missing_padding)
         //   println("'='.toByte()="+'='.toByte())
          //  val pad = '='.toByte() * (4-missing_padding).toByte()

         //   println("pad"+pad)
         //   println("valueBytes"+valueBytes)
        //    valueBytes = valueBytes.plus(pad.toByte())
          //  println("valueBytes"+valueBytes)
            Base64.getUrlDecoder().decode(valueBytes)
        } else {
           Base64.getDecoder().decode(valueBytes)
        }
        return encodedByte
    }

    /**
     * Convert a bytes to base 64 string.
     *
     * @param bytes   input bytes array
     * @param urlSafe flag if needed to convert to urlsafe presentation
     * @return base64 presentation
     */
    fun bytesToB64(bytes: ByteArray?, urlSafe: Boolean): String? {
        if (bytes == null) {
            return null
        }
        val decodedByte: ByteArray = if (urlSafe) {
            Base64.getUrlEncoder().encode(bytes)
        } else {
            Base64.getEncoder().encode(bytes)
        }
        println("bytesToB64 "+decodedByte)
        return  StringUtils.bytesToString(decodedByte, StringUtils.CODEC.US_ASCII)
    }

    /**
     * Convert a base 58 string to bytes.
     *
     *
     * Small cache provided for key conversions which happen frequently in pack
     * and unpack and message handling.
     */
    fun b58ToBytes(value: String): ByteArray {
        return Base58.decode(value)
    }

    /**
     * Convert a byte string to base 58.
     * Small cache provided for key conversions which happen frequently in pack
     * and unpack and message handling.
     */
    fun bytesToB58(value: ByteArray): String {
        return Base58.encode(value)
    }

    /**
     * Create a public and private signing keypair from a seed value.
     *
     * @param seed (bytes) Seed for keypair
     * @return A tuple of (public key, secret key)
     */
    @Throws(SiriusCryptoError::class, SodiumException::class)
    fun createKeypair(seed: ByteArray?): KeyPair {
        println("createKeypair seed="+seed)
        //  Sodium.crypto_sign_seed_keypair()
        var seed = seed
        if (seed != null) {
            validateSeed(seed)
        } else {
            seed = randomSeed()
        }

        return LibSodium.getInstance().cryptoSignSeedKeypair(seed)
    }

    /**
     * Generate a random seed value.
     *
     * @return A new random seed
     */
    fun randomSeed(): ByteArray? {
        return LibSodium.getInstance().randomBytesBuf(crypto_secretbox_KEYBYTES)

        //   return new Random().randomBytes(Sodium.crypto_secretbox_keybytes());
    }
    /**
     * Convert a seed parameter to standard format and check length.
     *
     * @param message The seed to validate
     * @return The validated and encoded seed
     */
    @Throws(SiriusCryptoError::class)
    fun validateSeed(message: String?): ByteArray? {
        if (message == null) {
            return null
        }
        val bytes: ByteArray
        if (message.contains("=")) {
            bytes = b64ToBytes(message, false)
        } else {
            val codec = StringCodec()
            bytes =  codec.fromASCIIStringToByteArray(message)
        }
        return validateSeed(bytes)
    }

    /**
     * Convert a seed parameter to standard format and check length.
     *
     * @param bytes The seed to validate
     * @return The validated and encoded seed
     */
    @Throws(SiriusCryptoError::class)
    fun validateSeed(bytes: ByteArray?): ByteArray? {
        if (bytes == null) {
            return null
        }
        if (bytes.size != 32) {
            throw SiriusCryptoError("Seed value must be 32 bytes in length")
        }
        return bytes
    }

    /**
     * Sign a message using a private signing key.
     * @param message The message to sign
     * @param secret The private signing key
     * @return The signature
     */
    fun signMessage(message: ByteArray, secret: ByteArray?): ByteArray {
       val  signedMessage = Signature.sign(message.toUByteArray(),secret?.toUByteArray() ?: UByteArray(0)).toByteArray()
      //  val signedMessage = ByteArray(Sign.BYTES + message.size)
        return signedMessage.copyOfRange(0, crypto_sign_BYTES)

        //return ByteArray(0)
    }

    /**
     * Verify a signed message according to a public verification key.
     * @param verkey The verkey to use in verification
     * @param message original message
     * @param signature signature
     * @return
     */
    fun verifySignedMessage(verkey: ByteArray?, message: ByteArray?, signature: ByteArray?): ByteArray {
        val signedMessage: ByteArray = signature?.plus(message?: ByteArray(0)) ?: ByteArray(0)
        try{
            val messageOpened =  Signature.open(signedMessage.toUByteArray(),verkey?.toUByteArray() ?: UByteArray(0))
            return messageOpened.toByteArray()
        }catch (e : Exception){
            e.printStackTrace()
        }
        return ByteArray(0)

    }

    fun didFromVerkey(verkey: ByteArray?): ByteArray? {
        return verkey?.copyOfRange(0, 16)
    }
}
