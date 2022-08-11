package com.sirius.library.n_wise

import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Ping
import com.sirius.library.agent.pairwise.TheirEndpoint
import com.sirius.library.hub.Context
import com.sirius.library.hub.coprotocols.CoProtocolP2PAnon
import com.sirius.library.messaging.Message
import com.sirius.library.n_wise.messages.BaseNWiseMessage
import com.sirius.library.n_wise.messages.Invitation
import com.sirius.library.n_wise.messages.LedgerUpdateNotify
import com.sirius.library.n_wise.transactions.InvitationTx
import com.sirius.library.n_wise.transactions.NWiseTx
import com.sirius.library.n_wise.transactions.RemoveParticipantTx
import com.sirius.library.utils.Base58
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.KeyPair
import com.sirius.library.utils.Logger
import com.sodium.LibSodium
import com.sodium.SodiumException

abstract class NWise {

    var log = Logger.getLogger(NWise::class.simpleName)
    var stateMachine: NWiseStateMachine? = null
    var myVerkey: ByteArray?= null
    abstract val ledgerType: String?
    abstract val restoreAttach: JSONObject?

    abstract fun fetchFromLedger(): Boolean
    protected abstract fun pushTransaction(tx: NWiseTx?): Boolean
    protected abstract val attach: JSONObject?
    val chatName: String?
        get() = stateMachine?.label

    fun createInvitation(context: Context<*>): Invitation? {
        val s = LibSodium.getInstance()
        try {

            val keyPair: KeyPair = s.cryptoSignKeypair()
            val invitationTx = InvitationTx()
            invitationTx.setPublicKeys(
                listOf(
                    keyPair.getPublicKey().asBytes
                )
            )
            invitationTx.sign(context.crypto, myDid?:"", myVerkey)
            pushTransaction(invitationTx)
            notify(context)
            return Invitation.builder().setLabel(chatName).setLedgerType(
                ledgerType
            ).setInvitationKeyId(Base58.encode(keyPair.getPublicKey().asBytes))
                .setInvitationPrivateKey(keyPair.getSecretKey().asBytes).setAttach(
                    attach
                ).build()
        } catch (e: SodiumException) {
            e.printStackTrace()
        }
        return null
    }

    val participants: List<NWiseParticipant>
        get() {
            fetchFromLedger()
            return stateMachine?.participants?: listOf()
        }
    val currentParticipantsVerkeysBase58: List<String>
        get() {
            val res: MutableList<String> = mutableListOf()
            for (p in participants) {
                res.add(Base58.encode(p.verkey))
            }
            return res
        }

    fun leave(context: Context<*>): Boolean {
        return removeParticipant(myDid, context)
    }

    fun removeParticipant(did: String?, context: Context<*>): Boolean {
        val tx = RemoveParticipantTx()
        tx.did = did
        tx.sign(context.crypto, myDid?:"", myVerkey)
        val res = pushTransaction(tx)
        if (res) notify(context)
        return res
    }

    val myDid: String?
        get() = stateMachine?.resolveDid(myVerkey)

    fun resolveNickname(verkeyBase58: String): String? {
        return stateMachine?.resolveNickname(Base58.decode(verkeyBase58))
    }

    fun resolveParticipant(verkeyBase58: String): NWiseParticipant? {
        return stateMachine?.resolveParticipant(Base58.decode(verkeyBase58))
    }

    fun send(message: Message, context: Context<*>): Boolean {
        val participants: List<NWiseParticipant> = participants
        for (participant in participants) {
            if (java.util.Arrays.equals(participant.verkey, myVerkey)) break
            val theirEndpoint = TheirEndpoint(
                participant.endpoint,
                Base58.encode(participant.verkey),
                listOf()
            )
            CoProtocolP2PAnon(
                context,
                Base58.encode(myVerkey!!),
                theirEndpoint,
                listOf(com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message.PROTOCOL),
                timeToLiveSec
            ).apply {
                send(message)
            }
        }
        return true
    }

    fun notify(context: Context<*>): Boolean {
        return send(LedgerUpdateNotify.builder().build(), context)
    }

    companion object {
        var protocols: List<String> =
           listOf(BaseNWiseMessage.PROTOCOL, Ack.PROTOCOL, Ping.PROTOCOL)
        var timeToLiveSec = 60
        fun restore(info: NWiseList.NWiseInfo): NWise? {
            return if (info.ledgerType.equals("iota@v1.0")) {
                IotaNWise.restore(info.attach?: JSONObject())
            } else null
        }
    }
}
