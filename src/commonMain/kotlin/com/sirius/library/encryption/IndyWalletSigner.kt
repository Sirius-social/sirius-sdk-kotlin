package com.sirius.library.encryption

import com.sirius.library.agent.wallet.abstract_wallet.AbstractCrypto

open class IndyWalletSigner(crypto: AbstractCrypto, verkey: String) : ByteSigner(JWSAlgorithm.EdDSA) {
    var crypto: AbstractCrypto
    var verkey: String
 //   @Throws(java.security.GeneralSecurityException::class)
    protected fun sign(bytes: ByteArray?): ByteArray? {
        return crypto.cryptoSign(verkey, bytes)
    }

    init {
        this.crypto = crypto
        this.verkey = verkey
    }
}