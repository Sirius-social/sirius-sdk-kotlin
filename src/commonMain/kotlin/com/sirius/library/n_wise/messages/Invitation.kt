package com.sirius.library.n_wise.messages

import com.sirius.library.messaging.Message
import com.sirius.library.utils.Base58.decode
import com.sirius.library.utils.Base58.encode
import com.sirius.library.utils.JSONObject
import kotlin.reflect.KClass


class Invitation(msg: String) : BaseNWiseMessage(msg) {
    companion object {
        fun builder(): Builder<*> {
            return InvitationBuilder()
        }

    }

    val label: String?
        get() = messageObj.optString("label")
    val invitationPrivateKey: ByteArray
        get() = decode(messageObj.optString("invitationPrivateKeyBase58")!!)
    val invitationKeyId: String?
        get() = messageObj.optString("invitationKeyId")
    val ledgerType: String?
        get() = messageObj.optString("ledgerType")
    val attach: JSONObject?
        get() = messageObj.getJSONObject("attach")

    abstract class Builder<B : Builder<B>> :
        BaseNWiseMessage.Builder<B>() {
        var label: String? = null
        var invitationKeyId: String? = null
        var invitationPrivateKeyBase58: String? = null
        var ledgerType: String? = null
        var attach: JSONObject? = null
        fun setLabel(label: String?): B {
            this.label = label
            return self()
        }

        fun setInvitationKeyId(keyId: String?): B {
            invitationKeyId = keyId
            return self()
        }

        fun setInvitationPrivateKey(invitationPrivateKey: ByteArray?): B {
            invitationPrivateKeyBase58 = encode(invitationPrivateKey!!)
            return self()
        }

        fun setLedgerType(ledgerType: String?): B {
            this.ledgerType = ledgerType
            return self()
        }

        fun setAttach(attach: JSONObject?): B {
            this.attach = attach
            return self()
        }

        override fun generateJSON(): JSONObject {
            val jsonObject: JSONObject = super.generateJSON()
            put(label, "label", jsonObject)
            put(invitationKeyId, "invitationKeyId", jsonObject)
            put(invitationPrivateKeyBase58, "invitationPrivateKeyBase58", jsonObject)
            put(ledgerType, "ledgerType", jsonObject)
            put(attach, "attach", jsonObject)
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
