package com.sirius.library.n_wise.transactions

import com.sirius.library.agent.wallet.abstract_wallet.AbstractCrypto
import com.sirius.library.encryption.IndyWalletSigner
import com.sirius.library.utils.Base58.decode
import com.sirius.library.utils.Base58.encode
import com.sirius.library.utils.JSONObject
import com.sodium.LibSodium


open class NWiseTx : JSONObject {
    constructor() : super() {}
    constructor(str: String?) : super(str) {}

    val previousTxHash: ByteArray
        get() {
            val hash: String = optString("previousTxHashBase58")?:""
            return if (!hash.isEmpty()) {
                decode(hash)
            } else ByteArray(0)
        }
    val previousTxHashBase58: String
        get() = optString("previousTxHashBase58")?:""
    val hash: ByteArray
        get() {
            val s = LibSodium.getInstance()
            val inputBytes: ByteArray = toString().getBytes(StandardCharsets.UTF_8)
            val outputBytes = ByteArray(SHA256_BYTES)
            s.cryptoHashSha256(outputBytes, inputBytes, inputBytes.size)
            return outputBytes
        }

    fun sign(crypto: AbstractCrypto, did: String, verkey: ByteArray?) {
        if (has("proof")) remove("proof")
        val byteSigner: ByteSigner = IndyWalletSigner(crypto, encode(verkey!!))
        val ldSigner: LdSigner<JcsEd25519Signature2020SignatureSuite> =
            JcsEd25519Signature2020LdSigner(byteSigner)
        ldSigner.setVerificationMethod(URI.create("$did#1"))
        val jsonLdObject: JsonLDObject = JsonLDObject.fromJson(this.toString())
        try {
            val proof = JSONObject(ldSigner.sign(jsonLdObject).toJson())
            put("proof", proof)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun sign(id: String?, privateKey: ByteArray?) {
        if (has("proof")) remove("proof")
        val ldSigner: LdSigner<JcsEd25519Signature2020SignatureSuite> =
            JcsEd25519Signature2020LdSigner(privateKey)
        ldSigner.setVerificationMethod(URI.create(id))
        val jsonLdObject: JsonLDObject = JsonLDObject.fromJson(this.toString())
        try {
            val proof = JSONObject(ldSigner.sign(jsonLdObject).toJson())
            put("proof", proof)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
