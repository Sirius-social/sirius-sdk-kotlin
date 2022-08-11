package com.sodium

import com.ionspin.kotlin.crypto.aead.AuthenticatedEncryptionWithAssociatedData
import com.ionspin.kotlin.crypto.box.Box
import com.ionspin.kotlin.crypto.secretstream.SecretStream
import com.ionspin.kotlin.crypto.signature.Signature
import com.ionspin.kotlin.crypto.util.LibsodiumRandom
import com.sirius.library.utils.Key
import com.sirius.library.utils.KeyPair
import com.sirius.library.utils.StringUtils

class LibSodium {

    companion object {
       fun  getInstance() :LibSodium{
           return LibSodium()
       }
    }

    fun cryptoSecretStreamKeygen(): Key {
        val bytes = SecretStream.xChaCha20Poly1305Keygen().toByteArray()
        return Key.fromBytes(bytes)
    }

    fun convertKeyPairEd25519ToCurve25519(ed25519KeyPair: KeyPair): KeyPair {
        val edPkBytes: ByteArray = ed25519KeyPair.getPublicKey().asBytes
        val edSkBytes: ByteArray = ed25519KeyPair.getSecretKey().asBytes
      //  val curvePkBytes = ByteArray(Sign.CURVE25519_PUBLICKEYBYTES)
    //    val curveSkBytes = ByteArray(Sign.CURVE25519_SECRETKEYBYTES)
        val curvePkBytes: ByteArray = convertPublicKeyEd25519ToCurve25519(edPkBytes)
        val curveSkBytes: ByteArray = convertSecretKeyEd25519ToCurve25519(edSkBytes)
        //if (!pkSuccess || !skSuccess) {
           // throw SodiumException("Could not convert this key pair.")
     //   }
        return KeyPair(Key.fromBytes(curvePkBytes), Key.fromBytes(curveSkBytes))
    }

    fun convertPublicKeyEd25519ToCurve25519( ed: ByteArray): ByteArray {
        return Signature.ed25519PkToCurve25519(ed.toUByteArray()).toByteArray()
    }

    fun convertSecretKeyEd25519ToCurve25519(ed: ByteArray): ByteArray {
        return Signature.ed25519SkToCurve25519(ed.toUByteArray()).toByteArray()
    }

    fun successful(res: Int): Boolean {
        return res == 0
    }

    fun cryptoBoxSeal( message: ByteArray,publicKey: ByteArray): ByteArray {
        return Box.seal(message.toUByteArray(), publicKey.toUByteArray()).toByteArray()
    }

    fun cryptoBox(
        cipherText: ByteArray,
        message: ByteArray,
        messageLen: Long,
        nonce: ByteArray,
        publicKey: ByteArray,
        secretKey: ByteArray
    ): ByteArray {
        if (messageLen < 0 || messageLen > message.size) {
         //   throw java.lang.IllegalArgumentException("messageLen out of bounds: $messageLen")
        }

        return Box.box(cipherText.toUByteArray(),message.toUByteArray(),nonce.toUByteArray(),publicKey.toUByteArray(),secretKey.toUByteArray()).toByteArray()
       // return ByteArray(0)
       // return successful(getSodium().crypto_box(cipherText, message, messageLen, nonce, publicKey, secretKey))
    }

    fun cryptoBoxOpen(
        message: ByteArray,
        cipherText: ByteArray,
        messageLen: Long,
        nonce: ByteArray,
        publicKey: ByteArray,
        secretKey: ByteArray
    ): ByteArray {
        if (messageLen < 0 || messageLen > message.size) {
            //   throw java.lang.IllegalArgumentException("messageLen out of bounds: $messageLen")
        }

        return Box.openBox(message.toUByteArray(),cipherText.toUByteArray(),nonce.toUByteArray(),publicKey.toUByteArray(),secretKey.toUByteArray()).toByteArray()
        // return ByteArray(0)
        // return successful(getSodium().crypto_box(cipherText, message, messageLen, nonce, publicKey, secretKey))
    }

    fun randomBytesBuf(size: Int): ByteArray {
      return  LibsodiumRandom.buf(size).toByteArray()
    }


    fun cryptoAeadChaCha20Poly1305IetfEncrypt(
        c: ByteArray?,
        cLen: LongArray?,
        m: ByteArray,
        mLen: Long,
        ad: ByteArray,
        adLen: Long,
        nSec: ByteArray?,
        nPub: ByteArray,
        k: ByteArray
    ): ByteArray {
        println("cryptoAeadChaCha20Poly1305IetfEncryp mes="+StringUtils.bytesToString(m, StringUtils.CODEC.US_ASCII))
        println("cryptoAeadChaCha20Poly1305IetfEncryp ad"+StringUtils.bytesToString(ad, StringUtils.CODEC.US_ASCII))
        println("cryptoAeadChaCha20Poly1305IetfEncryp nonce="+StringUtils.bytesToString(nPub, StringUtils.CODEC.US_ASCII))
        println("cryptoAeadChaCha20Poly1305IetfEncryp nonce="+nPub.size)
        println("cryptoAeadChaCha20Poly1305IetfEncryp Key="+StringUtils.bytesToString(k, StringUtils.CODEC.US_ASCII))
       return AuthenticatedEncryptionWithAssociatedData.chaCha20Poly1305IetfEncrypt(m.toUByteArray(),ad.toUByteArray(),nPub.toUByteArray(),k.toUByteArray()).toByteArray()

    }


    @Throws(SodiumException::class)
    fun cryptoSignSeedKeypair(seed: ByteArray?): KeyPair {
       val pair =   Signature.seedKeypair(seed?.toUByteArray() ?: UByteArray(0))
        println("cryptoSignSeedKeypair pair="+pair)
        return KeyPair(Key.fromBytes(pair.publicKey.toByteArray()), Key.fromBytes(pair.secretKey.toByteArray()))
    }


    @Throws(SodiumException::class)
    fun cryptoSignKeypair(): KeyPair {
        val pair =   Signature.keypair()
        println("cryptoSignKeypair pair="+pair)
        return KeyPair(Key.fromBytes(pair.publicKey.toByteArray()), Key.fromBytes(pair.secretKey.toByteArray()))
    }

}