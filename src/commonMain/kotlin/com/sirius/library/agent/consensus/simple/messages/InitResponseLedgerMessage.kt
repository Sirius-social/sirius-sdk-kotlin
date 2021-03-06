package com.sirius.library.agent.consensus.simple.messages

import com.sirius.library.messaging.Message
import com.sirius.library.utils.JSONObject
import kotlin.reflect.KClass

class InitResponseLedgerMessage(msg: String) : InitRequestLedgerMessage(msg) {
    companion object {
        fun builder(): Builder<*> {
            return InitResponseLedgerMessageBuilder()
        }


    }

    fun assignFrom(source: BaseInitLedgerMessage) {
        for (key in source.getMessageObjec().keySet()) {
            if (key == FIELD_ID || key == FIELD_TYPE || key == THREAD_DECORATOR) continue
            this.getMessageObjec().put(key, source.getMessageObjec().get(key))
        }
    }

    fun signature(did: String): JSONObject? {
        for (o in signatures()) {
            if ((o as JSONObject).optString("participant") == did) {
                return o as JSONObject
            }
        }
        return null
    }

    abstract class Builder<B : Builder<B>> :
        InitRequestLedgerMessage.Builder<B>() {
        override fun generateJSON(): JSONObject {
            return super.generateJSON()
        }

         override fun build(): InitResponseLedgerMessage {
            return InitResponseLedgerMessage(generateJSON().toString())
        }
    }

    private class InitResponseLedgerMessageBuilder :
        Builder<InitResponseLedgerMessageBuilder>() {
        protected override fun self(): InitResponseLedgerMessageBuilder {
            return this
        }

        override fun getClass(): KClass<out Message> {
            return InitResponseLedgerMessage::class
        }
    }
}
