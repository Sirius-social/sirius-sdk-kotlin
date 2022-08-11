package com.sirius.library.n_wise.transactions

import com.sirius.library.utils.JSONObject

class AddParticipantTx : NWiseTx {
    constructor() : super() {
        put("type", "addParticipantTx")
    }

    constructor(jsonObject: JSONObject) : super(jsonObject.toString()) {}

    var nickname: String?
        get() = optString("nickname")
        set(nickname) {
            put("nickname", nickname)
        }
    var did: String?
        get() = optString("did")
        set(did) {
            put("did", did)
        }
    var didDoc: JSONObject?
        get() = getJSONObject("didDoc")
        set(didDoc) {
            put("didDoc", didDoc)
        }
}
