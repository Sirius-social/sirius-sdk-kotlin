package com.sirius.library.n_wise.transactions

import com.sirius.library.agent.wallet.abstract_wallet.AbstractCrypto
import com.sirius.library.utils.multibase.Base58.decode
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.JcsEd25519Signature2020LdSigner
import com.sirius.library.utils.StringUtils
import com.sodium.LibSodium


open class NWiseTx : JSONObject {
    constructor() : super() {}
    constructor(str: String?) : super(str) {}

    val previousTxHash: ByteArray
        get() {
            val hash = optString("previousTxHashBase58")
            return if (!hash!!.isEmpty()) {
                decode(hash)
            } else ByteArray(0)
        }
    val previousTxHashBase58: String?
        get() = optString("previousTxHashBase58")
    val hash: ByteArray
        get() {
            val s: LibSodium = LibSodium.getInstance()
            val inputBytes: ByteArray = StringUtils.stringToBytes(toString(),StringUtils.CODEC.UTF_8)
            val outputBytes =   s.cryptoHashSha256(inputBytes)
            return outputBytes
        }

    fun sign(crypto: AbstractCrypto?, did: String, verkey: ByteArray?) {
        val signer = JcsEd25519Signature2020LdSigner()
        signer.setVerificationMethod("$did#1")
        signer.sign(this, verkey, crypto!!)
    }

    fun sign(id: String?, privateKey: ByteArray) {
        if (has("proof")) remove("proof")
        val signer = JcsEd25519Signature2020LdSigner()
        signer.setVerificationMethod(id?:"")
        signer.sign(this, privateKey)
    }
}
