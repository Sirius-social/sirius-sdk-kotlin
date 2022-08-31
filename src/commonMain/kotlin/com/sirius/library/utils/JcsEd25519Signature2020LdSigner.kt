package com.sirius.library.utils

import com.sirius.library.agent.wallet.abstract_wallet.AbstractCrypto
import com.sirius.library.utils.Base58.encode
import com.sodium.LibSodium


class JcsEd25519Signature2020LdSigner {
    var proof = JSONObject().put("type", "JcsEd25519Signature2020")
    fun setVerificationMethod(verificationMethod: URI) {
        proof.put("verificationMethod", verificationMethod.toString())
    }

    fun setCreator(creator: URI) {
        proof.put("creator", creator.toString())
    }

    fun sign(jsonDoc: JSONObject, publicKey: ByteArray?, crypto: AbstractCrypto) {
        val digest = prepare(jsonDoc)
        val signature = crypto.cryptoSign(encode(publicKey!!), digest)
        proof.put("signatureValue", encode(signature!!))
    }

    fun sign(jsonDoc: JSONObject, privateKey: ByteArray?) {
        val digest = prepare(jsonDoc)
        val s: LazySodiumJava = LibSodium.getInstance().getLazySodium()
        val signature = ByteArray(Sign.BYTES)
        s.cryptoSignDetached(signature, digest, digest.size, privateKey)
        proof.put("signatureValue", encode(signature))
    }

    private fun prepare(jsonDoc: JSONObject): ByteArray {
        if (jsonDoc.has("proof")) jsonDoc.remove("proof")
        jsonDoc.put("proof", proof)
        return try {
            val jc = JsonCanonicalizer(jsonDoc.toString())
            val canonicalized: String = jc.getEncodedString()
            MessageDigest.getInstance("SHA-256").digest(canonicalized.toByteArray())
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        }
    }
}
