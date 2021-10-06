package com.sirius.library.agent.aries_rfc.feature_0095_basic_message

import com.sirius.library.agent.aries_rfc.AriesProtocolMessage
import com.sirius.library.agent.aries_rfc.concept_0017_attachments.Attach
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject

class Message(msg: String) : AriesProtocolMessage(msg) {
    companion object {
        fun builder(): Builder<*> {
            return MessageBuilder()
        }

        init {
            com.sirius.library.messaging.Message.registerMessageClass(Message::class, "basicmessage", "message")
        }
    }

    val content: String?
        get() = getMessageObj().optString("content")
    val attaches: List<Any>
        get() {
            val res: MutableList<Attach> = ArrayList<Attach>()
            if (messageObjectHasKey("~attach")) {
                val arr: JSONArray = getMessageObj().getJSONArray("~attach") ?: JSONArray()
                for (o in arr) {
                    res.add(Attach(o as JSONObject))
                }
            }
            return res
        }

    fun addAttach(att: Attach) {
        if (!messageObjectHasKey("~attach")) {
            getMessageObj().put("~attach", JSONArray())
        }
        getMessageObj().getJSONArray("~attach")?.put(att)
    }

    abstract class Builder<B : Builder<B>> : AriesProtocolMessage.Builder<B>() {
        var locale: String? = null
        var content: String? = null
        fun setLocale(locale: String?): B {
            this.locale = locale
            return self()
        }

        fun setContent(content: String?): B {
            this.content = content
            return self()
        }

         override fun generateJSON(): JSONObject {
            val jsonObject: JSONObject = super.generateJSON()
            val id: String? = jsonObject.optString("id")
            if (locale != null) {
                jsonObject.put("~l10n", JSONObject().put("locale", locale))
            }
            if (content != null) {
                jsonObject.put("content", content)
            }
            return jsonObject
        }

        fun build(): Message {
            return Message(generateJSON().toString())
        }
    }

    private class MessageBuilder : Builder<MessageBuilder>() {
        protected override fun self(): MessageBuilder {
            return this
        }
    }
}
