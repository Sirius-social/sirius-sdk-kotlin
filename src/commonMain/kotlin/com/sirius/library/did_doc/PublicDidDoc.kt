package com.sirius.library.did_doc

import com.sirius.library.agent.aries_rfc.DidDoc
import com.sirius.library.agent.wallet.abstract_wallet.AbstractNonSecrets
import com.sirius.library.agent.wallet.abstract_wallet.model.RetrieveRecordOptions
import com.sirius.library.hub.Context
import com.sirius.library.utils.JSONObject


abstract class PublicDidDoc : DidDoc() {
    abstract fun submitToLedger(context: Context<*>): Boolean
    fun saveToWallet(nonSecrets: AbstractNonSecrets) {
        val tags: JSONObject = JSONObject().put("tag1", NON_SECRET_WALLET_NAME).put("id", getDid())
        val opts = RetrieveRecordOptions(false, false, false)
        if (nonSecrets.walletSearch(
                NON_SECRET_WALLET_NAME,
                tags.toString(),
                opts,
                1
            ).second === 0
        ) {
            nonSecrets.addWalletRecord(
                NON_SECRET_WALLET_NAME,
                getDid(),
                payload.toString(),
                tags.toString()
            )
        } else {
            nonSecrets.updateWalletRecordValue(NON_SECRET_WALLET_NAME, getDid(), payload.toString())
        }
    }

    companion object {
        const val NON_SECRET_WALLET_NAME = "PublicDidDoc"
    }
}
