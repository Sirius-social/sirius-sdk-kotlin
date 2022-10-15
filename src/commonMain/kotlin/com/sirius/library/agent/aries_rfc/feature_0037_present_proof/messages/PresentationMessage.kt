package com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages

import com.sirius.library.messaging.Message
import com.sirius.library.utils.multibase.Base64
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.UUID
import kotlin.reflect.KClass

class PresentationMessage(msg: String) : BasePresentProofMessage(msg) {
    companion object {
        fun builder(): Builder<*> {
            return PresentationMessageBuilder()
        }

    }

    fun proof(): JSONObject {
        val obj: Any? = getMessageObjec().get("presentations~attach")
        var attach: JSONObject? = null
        if (obj is JSONArray && !(obj as JSONArray).isEmpty()) {
            attach = (obj as JSONArray).getJSONObject(0)
        }
        if (obj is JSONObject) {
            attach = obj as JSONObject
        }
        if (attach != null && attach.has("data") && attach.getJSONObject("data")?.has("base64") ==true) {
            val rawBase64: String = attach.getJSONObject("data")?.getString("base64") ?:""
            return JSONObject(Base64.getDecoder().decode(rawBase64).decodeToString())
        }
        return JSONObject()
    }

    abstract class Builder<B : Builder<B>> : BasePresentProofMessage.Builder<B>() {
        var proof: JSONObject? = null
        var presentationId: String? = null
        fun setProof(proof: JSONObject?): B {
            this.proof = proof
            return self()
        }

        fun setPresentationId(presentationId: String?): B {
            this.presentationId = presentationId
            return self()
        }

        override fun generateJSON(): JSONObject {
            val jsonObject: JSONObject = super.generateJSON()
            val id: String? = jsonObject.optString("id")
            if (proof != null) {
                presentationId = if (presentationId != null) presentationId else UUID.randomUUID.toString()
                val attach = JSONObject()
                attach.put("@id", "libindy-presentation-$presentationId")
                attach.put("mime-type", "application/json")
                val data = JSONObject()
                val base64: ByteArray = Base64.getEncoder()
                    .encode(proof.toString().encodeToByteArray())
                data.put("base64", base64.decodeToString())
                attach.put("data", data)
                val arr = JSONArray()
                arr.put(attach)
                jsonObject.put("presentations~attach", arr)
            }
            return jsonObject
        }

        fun build(): PresentationMessage {
            return PresentationMessage(generateJSON().toString())
        }
    }

    private class PresentationMessageBuilder : Builder<PresentationMessageBuilder>() {
        protected override fun self(): PresentationMessageBuilder {
            return this
        }
        override fun getClass(): KClass<out Message> {
            return PresentationMessage::class
        }
    }
}
