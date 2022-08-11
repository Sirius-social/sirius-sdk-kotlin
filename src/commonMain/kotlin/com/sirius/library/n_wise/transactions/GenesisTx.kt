package com.sirius.library.n_wise.transactions

import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnProtocolMessage.Companion.buildDidDoc
import com.sirius.library.utils.Base58.decode
import com.sirius.library.utils.Base58.encode
import com.sirius.library.utils.JSONObject


class GenesisTx : NWiseTx {
    constructor() : super() {
        put("type", "genesisTx")
    }

    constructor(jsonObject: JSONObject) : super(jsonObject.toString()) {}

    var label: String?
        get() = optString("label")
        set(label) {
            put("label", label)
        }
    var creatorNickname: String?
        get() = optString("creatorNickname")
        set(creatorNickname) {
            put("creatorNickname", creatorNickname)
        }
    val creatorDid: String?
        get() = getJSONObject("connection")?.optString("DID")
    val creatorVerkey: ByteArray
        get() = decode(
            getJSONObject("connection")?.optJSONObject("DIDDoc")?.optJSONArray("publicKey")
                ?.getJSONObject(0)?.optString("publicKeyBase58") ?:""
        )
    val creatorDidDoc: JSONObject?
        get() = getJSONObject("connection")?.optJSONObject("DIDDoc")

    fun setCreatorDidDocParams(
        did: String,
        verkey: ByteArray?,
        endpoint: String?,
        connectionServices: List<JSONObject?>,
        didDocExtra: JSONObject?
    ) {
        put(
            "connection", JSONObject().put("DID", did).put(
                "DIDDoc", buildDidDoc(
                    did, encode(
                        verkey!!
                    ), endpoint, didDocExtra
                )
            )
        )
        for (s in connectionServices) {
            getJSONObject("connection")?.getJSONObject("DIDDoc")?.getJSONArray("service")?.put(s)
        }
    }

    fun setCreatorDidDocParams(did: String, verkey: ByteArray?, endpoint: String?) {
        setCreatorDidDocParams(did, verkey, endpoint, listOf(), JSONObject())
    }
}
