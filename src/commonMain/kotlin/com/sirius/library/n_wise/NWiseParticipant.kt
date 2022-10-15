package com.sirius.library.n_wise

import com.sirius.library.utils.multibase.Base58.decode
import com.sirius.library.utils.JSONObject


class NWiseParticipant {
    var nickname: String? = null
    var did: String? = null
    var didDoc: JSONObject? = null
    val endpoint: String?
        get() = didDoc?.optJSONArray("service")?.optJSONObject(0)?.optString("serviceEndpoint")
    val verkey: ByteArray
        get() = decode(
            didDoc?.optJSONArray("publicKey")?.optJSONObject(0)?.optString("publicKeyBase58")?:""
        )
}