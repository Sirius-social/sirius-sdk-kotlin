package com.sirius.library.n_wise.messages

import com.sirius.library.utils.JSONObject

class IotaResponseAttach : JSONObject {
    constructor(tag: String?) : super() {
        put("tag", tag)
    }

    constructor(o: JSONObject) : super(o.toString()) {}

    val tag: String?
        get() = getString("tag")
}