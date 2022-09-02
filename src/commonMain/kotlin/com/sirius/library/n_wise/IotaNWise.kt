package com.sirius.library.n_wise

import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnProtocolMessage
import com.sirius.library.hub.Context
import com.sirius.library.n_wise.messages.Invitation
import com.sirius.library.n_wise.messages.IotaResponseAttach
import com.sirius.library.n_wise.transactions.AddParticipantTx
import com.sirius.library.n_wise.transactions.GenesisTx
import com.sirius.library.n_wise.transactions.NWiseTx
import com.sirius.library.utils.Base58.decode
import com.sirius.library.utils.Base58.encode
import com.sirius.library.utils.IotaUtils
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.StringUtils
import org.iota.client.Client
import org.iota.client.MessageId


class IotaNWise(stateMachine: NWiseStateMachine?, myVerkey: ByteArray) :
    NWise() {
    override val restoreAttach: JSONObject
        get() = JSONObject().put("tag",IotaUtils. generateTag(stateMachine!!.genesisCreatorVerkey ?: ByteArray(0)))
            .put("myVerkeyBase58", encode(myVerkey ?: ByteArray(0)))

    override fun fetchFromLedger(): Boolean {
        stateMachine = processTransactions(IotaUtils.generateTag(stateMachine!!.genesisCreatorVerkey ?: ByteArray(0))).first
        return true
    }

    override fun pushTransaction(tx: NWiseTx?): Boolean {
        val tag: String = IotaUtils.generateTag(stateMachine!!.genesisCreatorVerkey ?: ByteArray(0))
        val (first, second) = pushTransactionToIota(tx, tag)
        stateMachine = second
        return first
    }

    override val attach: JSONObject
        protected get() = IotaResponseAttach(IotaUtils.generateTag(stateMachine!!.genesisCreatorVerkey ?: ByteArray(0)))
    override val ledgerType: String
        get() = "iota@v1.0"

    companion object {
        fun createChat(chatName: String?, myNickName: String?, context: Context<*>): IotaNWise? {
            val (first, second) = context.did.createAndStoreMyDid()
            val genesisTx = GenesisTx()
            genesisTx.label = chatName
            genesisTx.creatorNickname = myNickName
            genesisTx.setCreatorDidDocParams(
                first,
                decode(second),
                context.endpointAddressWithEmptyRoutingKeys,
               listOf(),
                JSONObject()
            )
            genesisTx.sign(context.crypto, first, decode(second))
            val iota: Client = IotaUtils.node()
            return try {
                val tag: String = IotaUtils. generateTag(decode(second))
                val o: JSONObject =
                    JSONObject().put("transaction", genesisTx).put("meta", JSONObject())
                iota.message().withIndexString(tag)
                    .withData(StringUtils.stringToBytes(o.toString(), StringUtils.CODEC.UTF_8)).finish()
                val stateMachine = NWiseStateMachine()
                stateMachine.append(genesisTx)
                IotaNWise(stateMachine, decode(second))
            } catch (ex: Exception) {
                null
            }
        }

        fun acceptInvitation(
            invitation: Invitation,
            nickname: String?,
            context: Context<*>
        ): IotaNWise? {
            val attach = IotaResponseAttach(invitation.attach ?: JSONObject())
            val (first, second) = context.did.createAndStoreMyDid()
            val tx = AddParticipantTx()
            tx.did = first
            val didDoc: JSONObject = ConnProtocolMessage.buildDidDoc(
                first,
                second,
                context.endpointAddressWithEmptyRoutingKeys
            )
            tx.didDoc = didDoc
            tx.nickname = nickname
            tx.sign(invitation.invitationKeyId, invitation.invitationPrivateKey)
            val (first1, second1) = pushTransactionToIota(tx, attach.tag?:"")
            return if (first1) {
                val nWise = IotaNWise(second1, decode(second))
                nWise.notify(context)
                nWise
            } else {
                null
            }
        }

        fun restore(attach: JSONObject): IotaNWise {
            val tag: String? = attach.optString("tag")
            val myVerkeyBase58: String = attach.optString("myVerkeyBase58")?:""
            val stateMachine = processTransactions(tag).first
            return IotaNWise(stateMachine, decode(myVerkeyBase58))
        }

        private fun processTransactions(tag: String?): Pair<NWiseStateMachine, String> {
            val stateMachine = NWiseStateMachine()
            var prevMessageId = ""
            return try {
                val fetchedMessageIds: Array<MessageId> =
                    IotaUtils.node().message.indexString(tag?:"")
                val map: HashMap<String, MutableList<org.iota.client.Message>> = HashMap()
                for (msgId in fetchedMessageIds) {
                    val msg: org.iota.client.Message = IotaUtils.node().message.data(msgId)
                    if (msg.payload()!=null) {
                        val obj = JSONObject(StringUtils.bytesToString(msg.payload()!!.asIndexation().data(),StringUtils.CODEC.UTF_8))
                        val previousMessageId: String =
                            obj.optJSONObject("meta")?.optString("previousMessageId", "") ?:""
                        if (!map.containsKey(previousMessageId)) map[previousMessageId] =
                            mutableListOf(msg) else map[previousMessageId]!!
                            .add(msg)
                    }
                }
                if (!map.containsKey("")) return Pair(stateMachine, "")
                var prevMessage: org.iota.client.Message? = null
                while (!map.isEmpty()) {
                    if (map.containsKey(prevMessageId)) {
                        val list: List<org.iota.client.Message> =
                            map[prevMessageId]?.filter { m ->
                                checkMessage(
                                    m,
                                    stateMachine
                                )
                            }?.sortedWith(IotaUtils.msgComparator).orEmpty()
                        if (list.isEmpty()) {
                            return Pair(stateMachine, prevMessageId)
                        } else {
                            map.remove(prevMessageId)
                            prevMessage = list[list.size - 1]
                            stateMachine.append(
                                JSONObject(StringUtils.bytesToString(prevMessage.payload()?.asIndexation()?.data() ?: ByteArray(0),
                                    StringUtils.CODEC.UTF_8)).
                                optJSONObject("transaction") ?: JSONObject()
                            )
                            prevMessageId = prevMessage.id().toString()
                        }
                    } else {
                        break
                    }
                }
                Pair(stateMachine, prevMessageId)
            } catch (e: Exception) {
                Pair(stateMachine, prevMessageId)
            }
        }

        private fun checkMessage(
            msg: org.iota.client.Message,
            stateMachine: NWiseStateMachine
        ): Boolean {
            return true
        }

        private fun pushTransactionToIota(
            tx: NWiseTx?,
            tag: String
        ): Pair<Boolean, NWiseStateMachine> {
            val (first, second) = processTransactions(tag)
            val o: JSONObject = JSONObject().put("transaction", tx).put(
                "meta", JSONObject().put(
                    "previousMessageId",
                    second
                )
            )
            try {
                if (!first.check(tx!!)) {
                    return Pair(false, first)
                }
                IotaUtils.node().message().withIndexString(tag)
                    .withData(StringUtils.stringToBytes(o.toString(),StringUtils.CODEC.UTF_8)).finish()
                first.append(tx)
            } catch (ex: Exception) {
                return Pair(false, first)
            }
            return Pair(true, first)
        }
    }

    init {
        this.stateMachine = stateMachine
        this.myVerkey = myVerkey!!
    }
}
