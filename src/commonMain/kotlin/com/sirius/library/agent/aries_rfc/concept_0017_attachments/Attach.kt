package com.sirius.library.agent.aries_rfc.concept_0017_attachments

import com.sirius.library.utils.multibase.Base64
import com.sirius.library.utils.JSONObject
import kotlinx.serialization.json.JsonObject

class Attach : JSONObject {
    constructor(obj: JSONObject) : super(obj.toString()) {}
    constructor(obj: JsonObject) : super(obj) {}
    constructor() : super() {}

    val id: String?
        get() = optString("@id")

    fun setId(id: String?): Attach {
        put("@id", id)
        return this
    }

    val mimeType: String?
        get() = optString("mime-type")

    fun setMimeType(mimeType: String?): Attach {
        put("mime-type", mimeType)
        return this
    }

    val fileName: String?
        get() = optString("filename")

    fun setFileName(fileName: String?): Attach {
        put("filename", fileName)
        return this
    }

    val lastModTime: String?
        get() = optString("lastmod_time")

    fun setLastModTime(lastModTime: String?): Attach {
        put("lastmod_time", lastModTime)
        return this
    }

    val description: String?
        get() = optString("description")

    fun setDescription(description: String?): Attach {
        put("description", description)
        return this
    }

    val data: ByteArray?
        get() = if (!has("data")) null else Base64.getDecoder()
            .decode(optJSONObject("data")?.optString("base64")?:"")

    fun setData(data: ByteArray): Attach {
        //TODO String from bytes
        put("data", JSONObject().put("base64", Base64.getEncoder().encode(data).decodeToString()))
        return this
    }
}
