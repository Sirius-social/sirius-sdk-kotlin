package com.sirius.library.n_wise.transactions

import com.sirius.library.utils.JSONObject

class RemoveParticipantTx : NWiseTx {
    constructor() : super() {
        put("type", "removeParticipantTx")
    }

    constructor(o: JSONObject) : super(o.toString()) {}

    var did: String?
        get() = optString("did")
        set(did) {
            put("did", did)
        }
}
