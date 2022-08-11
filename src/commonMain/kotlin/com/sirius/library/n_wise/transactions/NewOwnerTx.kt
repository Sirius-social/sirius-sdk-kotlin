package com.sirius.library.n_wise.transactions

import com.sirius.library.utils.JSONObject

class NewOwnerTx : NWiseTx {
    constructor() : super() {
        put("type", "newOwnerTx")
    }

    constructor(o: JSONObject) : super(o.toString()) {}

    var did: String?
        get() = optString("did")
        set(did) {
            put("did", did)
        }
}