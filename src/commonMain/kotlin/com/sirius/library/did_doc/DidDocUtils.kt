package com.sirius.library.did_doc

import com.sirius.library.agent.aries_rfc.DidDoc
import com.sirius.library.agent.wallet.abstract_wallet.AbstractNonSecrets
import com.sirius.library.agent.wallet.abstract_wallet.model.RetrieveRecordOptions
import com.sirius.library.did_doc.PublicDidDoc.Companion.NON_SECRET_WALLET_NAME
import com.sirius.library.utils.JSONObject


object DidDocUtils {
    fun resolve(did: String): PublicDidDoc? {
        return if (did.startsWith("did:iota:")) {
            IotaPublicDidDoc.load(did)
        } else null
    }

    fun publicDidList(ns: AbstractNonSecrets): List<String> {
        val query = JSONObject()
        query.put("tag1", NON_SECRET_WALLET_NAME)
        val opts = RetrieveRecordOptions(false, false, false)
        val (first) = ns.walletSearch(NON_SECRET_WALLET_NAME, query.toString(), opts, 10000)
        val res: MutableList<String> = ArrayList()
        for (s in first) {
           val id =  JSONObject(s).optString("id")
            id?.let {
                res.add(it)
            }

        }
        return res
    }

    fun fetchFromWallet(did: String?, ns: AbstractNonSecrets): DidDoc? {
        val record =
            ns.getWalletRecord(NON_SECRET_WALLET_NAME, did, RetrieveRecordOptions(true, true, true))
        return if (record != null) {
            DidDoc(JSONObject(JSONObject(record).optString("value")))
        } else null
    }
}
