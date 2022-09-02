package com.sirius.library.utils

import com.ionspin.kotlin.crypto.LibsodiumInitializer
import com.ionspin.kotlin.crypto.util.LibsodiumUtil
import com.sirius.library.utils.Base58.decode
import com.sodium.LibSodium


class JcsEd25519Signature2020LdVerifier(var publicKey: ByteArray) {
    fun verify(jsonDoc: JSONObject): Boolean {
        if (!jsonDoc.has("proof") || !jsonDoc.getJSONObject("proof")!!
                .has("signatureValue")
        ) return false
        val jsonDocCopy = JSONObject(jsonDoc.toString())
        val signature = decode(jsonDoc.getJSONObject("proof")!!.getString("signatureValue")!!)
        jsonDocCopy.getJSONObject("proof")!!.remove("signatureValue")
        var canonicalizer: JsonCanonicalizer? = null
        return try {
            canonicalizer = JsonCanonicalizer(jsonDocCopy.toString())
            val canonicalized: String = canonicalizer.getEncodedString()
            val digest: ByteArray =
                MessageDigest.getInstance("SHA-256").digest(canonicalized.toByteArray())
            val s: LibSodium = LibSodium.getInstance()
            s.cryptoSignVerifyDetached(signature, digest, publicKey)
        } catch (e: Exception) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        }
    }
}
