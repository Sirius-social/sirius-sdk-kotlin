package com.sirius.library.agent.wallet.impl.mobile

import com.sirius.library.agent.wallet.LocalWallet
import com.sirius.library.agent.wallet.abstract_wallet.AbstractNonSecrets
import com.sirius.library.agent.wallet.abstract_wallet.model.RetrieveRecordOptions
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject
import org.hyperledger.indy.sdk.non_secrets.WalletRecord
import org.hyperledger.indy.sdk.non_secrets.WalletSearch
import org.hyperledger.indy.sdk.wallet.Wallet

actual class NonSecretsMobile actual constructor(val wallet: LocalWallet) : AbstractNonSecrets() {

    actual var timeoutSec = 60L
    override fun addWalletRecord(type: String?, id: String?, value: String?, tags: String?) {
        try {
            WalletRecord.add(wallet, type, id, value, tags)
                .get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: java.lang.Exception) {
            if (e.message != null) {
                e.printStackTrace()
               // if (!e.message!!.contains("WalletItemAlreadyExists")) e.printStackTrace()
            } else {
                e.printStackTrace()
            }

        }
    }

    override fun updateWalletRecordValue(type: String?, id: String?, value: String?) {
        try {
            WalletRecord.updateValue(wallet, type, id, value)
                .get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun updateWalletRecordTags(type: String?, id: String?, tags: String?) {
        try {
            WalletRecord.updateTags(wallet, type, id, tags)
                .get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun addWalletRecordTags(type: String?, id: String?, tags: String?) {
        try {
            WalletRecord.addTags(wallet, type, id, tags).get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun deleteWalletRecord(type: String?, id: String?, tagNames: List<String?>?) {
        try {
            val arrayTag: String = JSONArray(tagNames).toString()
            WalletRecord.deleteTags(wallet, type, id, arrayTag)
                .get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun deleteWalletRecord(type: String?, id: String?) {
        try {
            WalletRecord.delete(wallet, type, id).get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun getWalletRecord(type: String?, id: String?, options: RetrieveRecordOptions?): String? {
        try {
            return WalletRecord.get(wallet, type, id, options?.serialize())
                .get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: java.lang.Exception) {
            if(e.message!=null){
                if (!e.message!!.contains("WalletItemNotFoundException")) e.printStackTrace()
            }else{
                e.printStackTrace()
            }

        }
        return null
    }


    override fun walletSearch(
        type: String?,
        query: String?,
        options: RetrieveRecordOptions?,
        limit: Int
    ): Pair<List<String>, Int> {
        options?.setRetrieveRecords(true)
        options?.setRetrieveTotalCount(true)
        val optionStr: String? = options?.serialize()
        try {
            val search: WalletSearch =
                WalletSearch.open(wallet, type, query, optionStr)
                    .get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
            val searchListString: String =
                search.fetchNextRecords(wallet, limit).get(timeoutSec.toLong(), java.util.concurrent.TimeUnit.SECONDS)
            WalletSearch.closeSearch(search)
            return if (searchListString == null) {
                Pair(listOf(), 0)
            } else {
                val searchObj: JSONObject = JSONObject(searchListString)
                val records: JSONArray = searchObj.optJSONArray("records") ?: JSONArray()
                val lis: MutableList<String> = ArrayList<String>()
                if (records != null) {
                    for (rec in records) {
                        lis.add(rec.toString())
                    }
                }
                val totalCount: Int = searchObj.optInt("totalCount") ?: 0
                Pair(lis, totalCount)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return Pair(listOf(), 0)
    }

}