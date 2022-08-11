package com.sirius.library.n_wise

import com.sirius.library.agent.wallet.abstract_wallet.AbstractNonSecrets
import com.sirius.library.agent.wallet.abstract_wallet.model.RetrieveRecordOptions
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.UUID


class NWiseList(var nonSecrets: AbstractNonSecrets) {
    class NWiseInfo {
        var internalId: String? = null
        var ledgerType: String? = null
        var attach: JSONObject? = null
    }

    fun add(nWise: NWise): String {
        val internalId: String = UUID.randomUUID.toString()
        val tags: JSONObject = JSONObject().put("type", NON_SECRET_NWISE_LIST)
        nonSecrets.addWalletRecord(
            NON_SECRET_NWISE_LIST, internalId,
            JSONObject().put("ledgerType", nWise.ledgerType)
                .put("restoreAttach", nWise.restoreAttach).toString(),
            tags.toString()
        )
        return internalId
    }

    fun clearList() {
        val list = nWiseInfoList
        for (info in list) {
            nonSecrets.deleteWalletRecord(NON_SECRET_NWISE_LIST, info.internalId)
        }
    }

    fun remove(internalId: String?) {
        nonSecrets.deleteWalletRecord(NON_SECRET_NWISE_LIST, internalId)
    }

    val nWiseInfoList: List<NWiseInfo>
        get() {
            val tags: JSONObject = JSONObject().put("type", NON_SECRET_NWISE_LIST)
            val (first, second) = nonSecrets.walletSearch(
                NON_SECRET_NWISE_LIST, tags.toString(),
                RetrieveRecordOptions(false, true, false), 100000
            )
            val res: MutableList<NWiseInfo> = ArrayList()
            if (second === 0) return res
            for (s in first) {
                val o = JSONObject(s)
                val info = NWiseInfo()
                info.internalId = o.optString("id")
                val value = JSONObject(o.optString("value"))
                info.ledgerType = value.optString("ledgerType")
                info.attach = value.optJSONObject("restoreAttach")
                res.add(info)
            }
            return res
        }

    fun getNWiseInfo(internalId: String?): NWiseInfo? {
        val res = nonSecrets.getWalletRecord(
            NON_SECRET_NWISE_LIST,
            internalId,
            RetrieveRecordOptions(true, true, true)
        )
        if (res == null || res.isEmpty()) return null
        val o = JSONObject(res)
        val info = NWiseInfo()
        info.internalId = internalId
        info.ledgerType = JSONObject(o.optString("value")).optString("ledgerType")
        info.attach = JSONObject(o.optString("value")).optJSONObject("restoreAttach")
        return info
    }

    fun addInvitationKey(internalId: String?, keyBase58: String?): Boolean {
        if (!hasInvitationKey(keyBase58)) {
            val tags: JSONObject = JSONObject().put("invitationKeyBase58", keyBase58)
            nonSecrets.addWalletRecord(
                NON_SECRET_NWISE_LIST_INVITATION_KEYS,
                keyBase58,
                JSONObject().put("internalId", internalId).toString(),
                tags.toString()
            )
        }
        return true
    }

    fun hasInvitationKey(keyBase58: String?): Boolean {
        val tags: JSONObject = JSONObject().put("invitationKeyBase58", keyBase58)
        val (_, second) = nonSecrets.walletSearch(
            NON_SECRET_NWISE_LIST_INVITATION_KEYS, tags.toString(),
            RetrieveRecordOptions(true, true, true), 1
        )
        return second !== 0
    }

    fun getNWiseInfoByInvitation(keyBase58: String?): NWiseInfo? {
        val tags: JSONObject = JSONObject().put("invitationKeyBase58", keyBase58)
        val (first, second) = nonSecrets.walletSearch(
            NON_SECRET_NWISE_LIST_INVITATION_KEYS, tags.toString(),
            RetrieveRecordOptions(true, true, true), 1
        )
        if (second > 0) {
            val id: String? = JSONObject(JSONObject(first[0]).getString("value")).optString("internalId")
            return getNWiseInfo(id)
        }
        return null
    }

    fun removeInvitationKey(keyBase58: String?): Boolean {
        if (!hasInvitationKey(keyBase58)) {
            return false
        }
        nonSecrets.deleteWalletRecord(NON_SECRET_NWISE_LIST_INVITATION_KEYS, keyBase58)
        return true
    }

    companion object {
        private const val NON_SECRET_NWISE_LIST = "NWiseList"
        private const val NON_SECRET_NWISE_LIST_INVITATION_KEYS = "NWiseListInvitationKeys"
    }
}
