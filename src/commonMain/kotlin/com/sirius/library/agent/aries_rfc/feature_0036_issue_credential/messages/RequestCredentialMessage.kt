package com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages

import com.sirius.library.messaging.Message
import com.sirius.library.utils.Base64
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject

class RequestCredentialMessage(message: String) : BaseIssueCredentialMessage(message) {
    companion object {
        fun builder(): Builder<*> {
            return RequestCredentialMessageBuilder()
        }

        init {
            Message.registerMessageClass(RequestCredentialMessage::class, PROTOCOL, "request-credential")
        }
    }

    fun credRequest(): JSONObject? {
        var request: JSONObject? = this.getJSONOBJECTFromJSON("requests~attach")
        if (request == null) {
            val arr: JSONArray = getJSONArrayFromJSON("requests~attach", JSONArray()) ?: JSONArray()
            if (arr.length() > 0) {
                request = arr.getJSONObject(0)
            }
        }
        if (request != null) {
            val base64: String = request.getJSONObject("data")?.getString("base64") ?:""
            val decoded: ByteArray = Base64.getDecoder().decode(base64)
            return JSONObject(String(decoded))
        }
        return null
    }

    abstract class Builder<B : Builder<B>> :
        BaseIssueCredentialMessage.Builder<B>() {
        var credRequest: JSONObject? = null
        fun setCredRequest(credRequest: JSONObject?): B {
            this.credRequest = credRequest
            return self()
        }

        override fun generateJSON(): JSONObject {
            val jsonObject: JSONObject = super.generateJSON()
            val id: String? = jsonObject.optString("id")
            if (credRequest != null) {
                val requestAttach = JSONObject()
                requestAttach.put("@id", "cred-request-$id")
                requestAttach.put("mime-type", "application/json")
                val data = JSONObject()
                val base64: ByteArray = Base64.getEncoder()
                    .encode(credRequest.toString().toByteArray(java.nio.charset.StandardCharsets.UTF_8))
                data.put("base64", String(base64))
                requestAttach.put("data", data)
                val arr = JSONArray()
                arr.put(requestAttach)
                jsonObject.put("requests~attach", arr)
            }
            return jsonObject
        }

        fun build(): RequestCredentialMessage {
            return RequestCredentialMessage(generateJSON().toString())
        }
    }

    private class RequestCredentialMessageBuilder :
        Builder<RequestCredentialMessageBuilder>() {
        override fun self(): RequestCredentialMessageBuilder {
            return this
        }
    }
}
