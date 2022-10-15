package com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages

import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.BaseIssueCredentialMessage
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.IssueCredentialMessage
import com.sirius.library.messaging.Message
import com.sirius.library.utils.multibase.Base64
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject
import kotlin.reflect.KClass


class PresentationAck(message: String) : Ack(message) {
    companion object {
        fun builder(): Builder<*> {
            return PresentationAckBuilder()
        }

    }



    abstract class Builder<B : Builder<B>> :
        Ack.Builder<B>() {


        override fun generateJSON(): JSONObject {
            return super.generateJSON()
        }

        override fun build(): PresentationAck {
            return PresentationAck(generateJSON().toString())
        }
    }

    private class PresentationAckBuilder : Builder<PresentationAckBuilder>() {


        override fun getClass(): KClass<out Message> {
            return PresentationAck::class
        }

        override fun self(): PresentationAckBuilder {
            return this
        }

    }
}
