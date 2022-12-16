package com.sirius.library.agent.connections

import com.sirius.library.base.WebSocketConnector
import com.sirius.library.encryption.P2PConnection
import com.sirius.library.errors.sirius_exceptions.SiriusFieldValueError
import com.sirius.library.messaging.Message
import com.sirius.library.rpc.AddressedTunnel
import com.sirius.library.utils.*


abstract class BaseAgentConnection {
    open var log: Logger = Logger.getLogger(AddressedTunnel::class.simpleName)

    companion object {
        val IO_TIMEOUT = 30

    }

    var MSG_TYPE_CONTEXT = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/context"
    var serverAddress: String? = null
    var credentials: ByteArray? = null
    var p2p: P2PConnection? = null

    var timeout = IO_TIMEOUT
    var connector: WebSocketConnector? = null

    open fun setTimeouti(timeout: Int) {
        if (timeout <= 0) {
            throw RuntimeException("Timeout must be > 0")
        }
        this.timeout = timeout
    }


    constructor(serverAddress: String, credentials: ByteArray?, p2p: P2PConnection?, timeout: Int) {
        this.serverAddress = serverAddress
        this.credentials = credentials
        this.p2p = p2p
        this.timeout = timeout
        connector = WebSocketConnector(
            this.timeout,
            StringUtils.CODEC.UTF_8,
            serverAddress,
            path() ?: "",
            credentials
        )
    }

    abstract fun path(): String?

    open fun setup(context: Message) {}




    open val isOpen: Boolean
        get() = connector?.isOpen ?: false

    open fun close() {
        connector!!.close()
    }


    @Throws(SiriusFieldValueError::class)
    open fun create() {
         val feat: CompletableFutureKotlin<ByteArray?>? = connector?.read()
        connector!!.open()
        var payload = ByteArray(0)
        try {
              payload = feat?.get(timeout.toLong()) ?:ByteArray(0)
        } catch (e: Exception) {
            ExceptionHandler.handleException(e)
            e.printStackTrace()
        }
        val msgString = payload.decodeToString()
        log.log(Logger.Level.INFO, "Received message: " + msgString);
        val context = Message(msgString)
        if (context.getType() == null) {
            throw SiriusFieldValueError("message @type is empty")
        }
        if (MSG_TYPE_CONTEXT != context.getType()) {
            throw SiriusFieldValueError("message @type not equal $MSG_TYPE_CONTEXT")
        }
        setup(context)
    }
}
