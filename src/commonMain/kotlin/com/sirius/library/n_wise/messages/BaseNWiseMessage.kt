package com.sirius.library.n_wise.messages

import com.sirius.library.agent.aries_rfc.AriesProtocolMessage
import com.sirius.library.utils.JSONObject


open class BaseNWiseMessage(msg: String) : AriesProtocolMessage(msg) {
    abstract class Builder<B : Builder<B>> protected constructor() :
        AriesProtocolMessage.Builder<B>() {
        override fun generateJSON(): JSONObject {
            return super.generateJSON()
        }
    }

    companion object {
        const val PROTOCOL = "n-wise"
    }
}