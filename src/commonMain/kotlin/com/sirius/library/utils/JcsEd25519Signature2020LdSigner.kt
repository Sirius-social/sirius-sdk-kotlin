package com.sirius.library.utils

import com.ionspin.kotlin.crypto.LibsodiumInitializer
import com.sirius.library.agent.wallet.abstract_wallet.AbstractCrypto
import com.sirius.library.utils.json_canonical.JsonCanonicalizer
import com.sirius.library.utils.multibase.Base58.encode
import com.sodium.LibSodium


class JcsEd25519Signature2020LdSigner {
    var proof = JSONObject().put("type", "JcsEd25519Signature2020")
    fun setVerificationMethod(verificationMethod: String) {
        proof.put("verificationMethod", verificationMethod)
    }

    fun setCreator(creator: String) {
        proof.put("creator", creator)
    }

    fun sign(jsonDoc: JSONObject, publicKey: ByteArray?, crypto: AbstractCrypto) {
        val digest = prepare(jsonDoc)
        val signature = crypto.cryptoSign(encode(publicKey!!), digest)
        proof.put("signatureValue", encode(signature!!))
    }

    fun sign(jsonDoc: JSONObject, privateKey: ByteArray) {
        val digest = prepare(jsonDoc)
        val s = LibSodium.getInstance()
      //  val signature = ByteArray(Sign.BYTES)
        val signature =  s.cryptoSignDetached(digest, privateKey)
        proof.put("signatureValue", encode(signature))
    }

    private fun prepare(jsonDoc: JSONObject): ByteArray {
        if (jsonDoc.has("proof")) jsonDoc.remove("proof")
        jsonDoc.put("proof", proof)
        return try {
            val jc = JsonCanonicalizer(jsonDoc.toString())
            val canonicalized: String = jc.encodedString
            LibSodium.getInstance().cryptoHashSha256(StringUtils.stringToBytes(canonicalized, StringUtils.CODEC.UTF_8))
           // MessageDigest.getInstance("SHA-256").digest(StringUtils.stringToBytes(canonicalized, StringUtils.CODEC.UTF_8))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
