package com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages

import com.sirius.library.errors.sirius_exceptions.SiriusValidationError
import com.sirius.library.messaging.Message
import com.sirius.library.utils.Base64
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.StringCodec
import kotlin.reflect.KClass

class Invitation(msg: String) : ConnProtocolMessage(msg) {
    companion object {
        fun builder(): Builder<*> {
            return InvitationBuilder()
        }


    }

    fun recipientKeys(): List<String> {
        val res: MutableList<String> = ArrayList<String>()
        if (getMessageObjec().has("recipientKeys")) {
            val jsonArr: JSONArray? = getMessageObjec().getJSONArray("recipientKeys")
            jsonArr?.let {
                for (obj in jsonArr) {
                    if(obj is String){
                        val element = obj as? String
                        element?.let {
                            res.add(element)
                        }
                    }
                }
            }
        }
        return res
    }

    fun endpoint(): String? {
        return getMessageObjec().optString("serviceEndpoint")
    }

    fun label(): String? {
        return getMessageObjec().optString("label")
    }

    @Throws(SiriusValidationError::class)
    override fun validate() {
        super.validate()
        if (!(getMessageObjec().has("label") &&
                    getMessageObjec().has("recipientKeys") &&
                    getMessageObjec().has("serviceEndpoint"))
        ) throw SiriusValidationError("Attribute is missing")
    }

    fun invitationUrl(): String {
        val codec = StringCodec()
        val escapedMessage =  StringCodec().escapeStringLikePython(getMessageObjec().toString())
        val b64Invite = codec.fromByteArrayToASCIIString(Base64.getUrlEncoder().encode(codec.fromASCIIStringToByteArray(escapedMessage)))
        return "?c_i=$b64Invite"
    }

    abstract class Builder<B : Builder<B>> : ConnProtocolMessage.Builder<B>() {
        var label: String? = null
        var recipientKeys: List<String>? = null
        var endpoint: String? = null
        var routingKeys: List<String>? = null
        var did: String? = null
        fun setDid(did: String?): B {
            this.did = did
            return self()
        }

        fun setLabel(label: String?): B {
            this.label = label
            return self()
        }

        fun setRecipientKeys(recipientKeys: List<String>?): B {
            this.recipientKeys = recipientKeys
            return self()
        }

        fun setEndpoint(endpoint: String?): B {
            this.endpoint = endpoint
            return self()
        }

        fun setRoutingKeys(routingKeys: List<String>?): B {
            this.routingKeys = routingKeys
            return self()
        }

        override fun generateJSON(): JSONObject {
            val jsonObject: JSONObject = super.generateJSON()
            val id: String? = jsonObject.optString("id")
            if (label != null) {
                jsonObject.put("label", label)
            }
            if (recipientKeys != null) {
                jsonObject.put("recipientKeys", recipientKeys)
            }
            if (endpoint != null) {
                jsonObject.put("serviceEndpoint", endpoint)
            }
            if (routingKeys != null) {
                jsonObject.put("routingKeys", routingKeys)
            }
            if (did != null) {
                jsonObject.put("did", did)
            }
            return jsonObject
        }

        fun build(): Invitation {
            return Invitation(generateJSON().toString())
        }
    }

    private class InvitationBuilder : Builder<InvitationBuilder>() {
        override fun self(): InvitationBuilder {
            return this
        }

        override fun getClass(): KClass<out Message> {
            return Invitation::class
        }
    }
}
