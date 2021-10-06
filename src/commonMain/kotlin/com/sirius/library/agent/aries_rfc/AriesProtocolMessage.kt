package com.sirius.library.agent.aries_rfc

import com.sirius.library.errors.sirius_exceptions.SiriusValidationError
import com.sirius.library.messaging.Message
import com.sirius.library.messaging.Type
import com.sirius.library.utils.JSONObject
import kotlinx.serialization.json.JsonObject

abstract class AriesProtocolMessage : Message {


    val THREAD_DECORATOR = "~thread"

    constructor() : super("{}")

    constructor(message: String) : super(message)



    @Throws(SiriusValidationError::class)
    open  fun validate() {
    }

    open fun getAckMessageId(): String? {
        val pleaseAck: JSONObject? = getJSONOBJECTFromJSON("~please_ack", "{}")
        return if (pleaseAck?.has("message_id")==true) {
            pleaseAck.optString("message_id")
        } else this.getId()
    }

    open fun hasPleaseAck(): Boolean {
        return getMessageObj()?.has("~please_ack") ?: false
    }

    open fun setPleaseAck(flag: Boolean) {
        if (flag) {
            val pleaseAck = JSONObject()
            pleaseAck.put("message_id", this.getId())
            getMessageObj()?.put("~please_ack", pleaseAck)
        } else {
            getMessageObj()?.remove("~please_ack")
        }
    }

    open fun getThreadId(): String? {
        return if (getMessageObj()?.has(THREAD_DECORATOR) == true && getMessageObj()?.optJSONObject(THREAD_DECORATOR)?.has("thid") ==true
        ) {
            getMessageObj()?.optJSONObject(THREAD_DECORATOR)?.optString("thid")
        } else null
    }

    open fun setThreadId(thid: String?) {
        val thread: JSONObject?
        if (getMessageObj()?.has(THREAD_DECORATOR) == true) {
            thread = getMessageObj()?.optJSONObject(THREAD_DECORATOR)
        } else {
            thread = JSONObject()
        }
        thread?.put("thid", thid)
        getMessageObj()?.put(THREAD_DECORATOR, thread)
    }

    abstract class Builder<B : Builder<B>> protected constructor() {
        var version = DEF_VERSION
        var docUri = ARIES_DOC_URI
        var id: String? = null
        fun setVersion(version: String): B {
            this.version = version
            return self()
        }

        fun setDocUri(docUri: String): B {
            this.docUri = docUri
            return self()
        }

        fun setId(id: String?): B {
            this.id = id
            return self()
        }

        protected abstract fun self(): B
        open fun generateJSON(): JSONObject {
            val jsonObject = JSONObject()
            val (first, second) = Message.getProtocolAndName(this.javaClass.getDeclaringClass() as java.lang.Class<out Message?>)
            jsonObject.put("@type", Type(docUri, first, version, second))
            jsonObject.put("@id", if (id == null) generateId() else id)
            return jsonObject
        }
    }

    companion object {
        const val DEF_VERSION = "1.0"
        const val ARIES_DOC_URI = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/"
    }
}

