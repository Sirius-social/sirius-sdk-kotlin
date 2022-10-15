package com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages

import com.sirius.library.errors.sirius_exceptions.SiriusValidationError
import com.sirius.library.messaging.Message
import com.sirius.library.utils.multibase.Base64
import com.sirius.library.utils.Date
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject
import kotlin.reflect.KClass

class OfferCredentialMessage(message: String) : BaseIssueCredentialMessage(message) {
    companion object {
        fun builder(): Builder<*> {
            return OfferCredentialMessageBuilder()
        }


    }

    class ParseResult {
        var offer: JSONObject? = null
        var offerBody: JSONObject? = null
        var credDefBody: JSONObject? = null
    }

    @Throws(SiriusValidationError::class)
    fun parse(): ParseResult {
        var offerAttaches: JSONArray? = getMessageObjec()?.getJSONArray("offers~attach")
        if (offerAttaches == null) {
            val att: JSONObject? = getMessageObjec()?.optJSONObject("offers~attach")
            if (att != null) {
                offerAttaches = JSONArray()
                offerAttaches.put(att)
            }
        }
        if (offerAttaches == null) {
            throw SiriusValidationError("Expected attribute \"offer~attach\" must contains cred-Offer and cred-Def")
        }
        val res = ParseResult()
        res.offer = offerAttaches.getJSONObject(0)
        for (i in 0 until offerAttaches.length()) {
            val attach: JSONObject? = offerAttaches.getJSONObject(i)
            if (attach?.has("data")==true && attach?.getJSONObject("data")?.has("base64") == true) {
                val rawBase64: String = attach?.getJSONObject("data")?.getString("base64") ?:""
                val payload = JSONObject(Base64.getDecoder().decode(rawBase64).decodeToString())
                val offerFields: Set<String> = HashSet<String>(
                    listOf(
                        "key_correctness_proof",
                        "nonce",
                        "schema_id",
                        "cred_def_id"
                    )
                )
                val credDefFields: Set<String> = HashSet<String>(
                    listOf(
                        "value",
                        "type",
                        "ver",
                        "schemaId",
                        "id",
                        "tag"
                    )
                )
                if (payload.keySet().containsAll(offerFields)) {
                    res.offerBody = JSONObject()
                    for (field in offerFields) {
                        res.offerBody!!.put(field, payload.get(field))
                    }
                }
                if (payload.keySet().containsAll(credDefFields)) {
                    res.credDefBody = JSONObject()
                    for (field in credDefFields) {
                        res.credDefBody!!.put(field, payload.get(field))
                    }
                }
            }
        }
        if (res.offerBody == null) {
            throw SiriusValidationError("Expected offer~attach must contains Payload with offer")
        }
        if (res.credDefBody == null) {
            throw SiriusValidationError("Expected offer~attach must contains Payload with cred_def data")
        }
        return res
    }

    @Throws(SiriusValidationError::class)
    fun offer(): JSONObject? {
        return parse().offerBody
    }

    @Throws(SiriusValidationError::class)
    fun credDef(): JSONObject? {
        return parse().credDefBody
    }

    fun schema(): JSONObject? {
        val attaches: JSONArray = getMessageObjec().getJSONArray("~attach") ?: JSONArray()
        for (o in attaches) {
            val item: JSONObject = o as JSONObject
            if (item.optString("@type") == ISSUER_SCHEMA_TYPE) {
                return item.getJSONObject("data")?.getJSONObject("json")
            }
        }
        return null
    }

    val credentialPreview: List<ProposedAttrib>
        get() {
            val res: MutableList<ProposedAttrib> = ArrayList<ProposedAttrib>()
            val credentialPreview: JSONObject? = getMessageObjec().optJSONObject("credential_preview")
            if (credentialPreview != null) {
                if (credentialPreview.optString("@type") == CREDENTIAL_PREVIEW_TYPE) {
                    val attribs: JSONArray? = credentialPreview.optJSONArray("attributes")
                    if (attribs != null) {
                        for (o in attribs) {
                            res.add(ProposedAttrib(o as JSONObject))
                        }
                    }
                }
            }
            return res
        }

    abstract class Builder<B : Builder<B>> :
        BaseIssueCredentialMessage.Builder<B>() {
        var offer: JSONObject? = null
        var credDef: JSONObject? = null
        var translation: List<AttribTranslation>? = null
        var preview: List<ProposedAttrib>? = null
        var issuerSchema: JSONObject? = null
        var expiresTime: Date? = null
        fun setOffer(offer: JSONObject?): B {
            this.offer = offer
            return self()
        }

        fun setCredDef(credDef: JSONObject?): B {
            this.credDef = credDef
            return self()
        }

        fun setIssuerSchema(issuerSchema: JSONObject?): B {
            this.issuerSchema = issuerSchema
            return self()
        }

        fun setTranslation(translation: List<AttribTranslation>?): B {
            this.translation = translation
            return self()
        }

        fun setPreview(preview: List<ProposedAttrib>?): B {
            this.preview = preview
            return self()
        }

        fun setExpiresTime(expiresTime: Date?): B {
            this.expiresTime = expiresTime
            return self()
        }

        override fun generateJSON(): JSONObject {
            val jsonObject: JSONObject = super.generateJSON()
            val id: String? = jsonObject.optString("id")
            if (preview != null && !preview!!.isEmpty()) {
                val credPreview = JSONObject()
                credPreview.put("@type", CREDENTIAL_PREVIEW_TYPE)
                val attributes = JSONArray()
                for (attrib in preview!!) attributes.put(attrib)
                credPreview.put("attributes", attributes)
                jsonObject.put("credential_preview", credPreview)
            }
            if (offer != null && credDef != null) {
                val payload = JSONObject()
                for (key in JSONObject.getNames(offer!!)) payload.put(key, offer!!.get(key))
                for (key in JSONObject.getNames(credDef!!)) payload.put(key, credDef!!.get(key))
                val offersAttach = JSONObject()
                offersAttach.put("@id", "libindy-cred-offer-$id")
                offersAttach.put("mime-type", "application/json")
                val data = JSONObject()
                val base64: ByteArray = Base64.getEncoder()
                    .encode(payload.toString().encodeToByteArray())
                data.put("base64", base64.decodeToString())
                offersAttach.put("data", data)
                val attaches = JSONArray()
                attaches.put(offersAttach)
                jsonObject.put("offers~attach", attaches)
            }
            if (translation != null && !translation!!.isEmpty()) {
                if (!jsonObject.has("~attach")) jsonObject.put("~attach", JSONArray())
                val attach = JSONObject()
                attach.put("@type", CREDENTIAL_PREVIEW_TYPE)
                attach.put("id", CREDENTIAL_TRANSLATION_ID)
                val l10n = JSONObject()
                l10n.put("locale", locale)
                attach.put("~l10n", l10n)
                attach.put("mime-type", "application/json")
                val data = JSONObject()
                val transArr = JSONArray()
                for (trans in translation!!) {
                    transArr.put(trans.getDicti())
                }
                data.put("json", transArr)
                attach.put("data", data)
                val attaches: JSONArray? = jsonObject.getJSONArray("~attach")
                attaches?.put(attach)
            }
            if (issuerSchema != null) {
                if (!jsonObject.has("~attach")) jsonObject.put("~attach", JSONArray())
                val attach = JSONObject()
                attach.put("@type", ISSUER_SCHEMA_TYPE)
                attach.put("id", ISSUER_SCHEMA_ID)
                attach.put("mime-type", "application/json")
                val data = JSONObject()
                data.put("json", issuerSchema)
                attach.put("data", data)
                val attaches: JSONArray? = jsonObject.getJSONArray("~attach")
                attaches?.put(attach)
            }
            if (expiresTime != null) {
                val timing = JSONObject()
                timing.put("expires_time",  expiresTime!!.formatTo("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
                jsonObject.put("~timing", timing)
            }
            return jsonObject
        }

        fun build(): OfferCredentialMessage {
            return OfferCredentialMessage(generateJSON().toString())
        }
    }

    private class OfferCredentialMessageBuilder :
        Builder<OfferCredentialMessageBuilder>() {
        override fun self(): OfferCredentialMessageBuilder {
            return this
        }

        override fun getClass(): KClass<out Message> {
            return OfferCredentialMessage::class
        }
    }
}
