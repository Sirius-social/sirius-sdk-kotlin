package com.sirius.library.n_wise.transactions

import com.sirius.library.utils.multibase.Base58.decode
import com.sirius.library.utils.multibase.Base58.encode
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject


class InvitationTx : NWiseTx {
    constructor() : super() {
        put("type", "invitationTx")
    }

    constructor(jsonObject: JSONObject) : super(jsonObject.toString()) {}

    fun setPublicKeys(keys: List<ByteArray?>) {
        val jsonKeys = JSONArray()
        for (b in keys) {
            jsonKeys.put(
                JSONObject().put("id", encode(b!!)).put("type", "Ed25519VerificationKey2018").put(
                    "publicKeyBase58", encode(
                        b
                    )
                )
            )
        }
        put("publicKey", jsonKeys)
    }

    val publicKeys: List<Pair<String, ByteArray>>
        get() {
            val res: MutableList<Pair<String, ByteArray>> = ArrayList()
            val jsonKeys: JSONArray = optJSONArray("publicKey") ?: JSONArray()
            for (o in jsonKeys) {
                val jsonObject: JSONObject = o as JSONObject
                res.add(
                    Pair(
                        jsonObject.optString("id")?:"",
                        decode(jsonObject.optString("publicKeyBase58")?:"")
                    )
                )
            }
            return res
        }
}
