package com.sirius.library.n_wise.messages

import com.sirius.library.messaging.Message
import com.sirius.library.utils.JSONObject
import kotlin.reflect.KClass

class LedgerUpdateNotify(msg: String) : BaseNWiseMessage(msg) {
    companion object {
        fun builder(): Builder<*> {
            return LedgerUpdateNotifyBuilder()
        }


    }

    abstract class Builder<B : Builder<B>> :
        BaseNWiseMessage.Builder<B>() {
        override fun generateJSON(): JSONObject {
            return super.generateJSON()
        }

        fun build(): LedgerUpdateNotify {
            return LedgerUpdateNotify(generateJSON().toString())
        }
    }

    private class LedgerUpdateNotifyBuilder :
        Builder<LedgerUpdateNotifyBuilder>() {
        override fun self(): LedgerUpdateNotifyBuilder {
            return this
        }

        override fun getClass(): KClass<out Message> {
            return LedgerUpdateNotify::class
        }
    }
}
