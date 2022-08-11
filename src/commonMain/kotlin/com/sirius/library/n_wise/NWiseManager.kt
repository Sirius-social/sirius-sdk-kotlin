package com.sirius.library.n_wise


import com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message
import com.sirius.library.hub.Context;
import com.sirius.library.n_wise.messages.Invitation
import com.sirius.library.utils.Base58

class NWiseManager(context: Context<*>) {
    private var nWiseMap: MutableMap<String?, NWise>? = null
        private get() {
            if (field == null) {
                field = mutableMapOf()
                loadFromWallet()
            }
            return field
        }
    var context: Context<*>
    fun loadFromWallet() {
        val infos = NWiseList(context.nonSecrets).nWiseInfoList
        for (info in infos) {
            val nWise = NWise.restore(info)
            if (nWise != null) {
                nWiseMap!![info.internalId] = nWise
            }
        }
    }

    fun create(nWiseName: String?, myName: String?): String? {
        val nWise: NWise? = IotaNWise.createChat(nWiseName, myName, context)
        return if (nWise != null) add(nWise) else null
    }

    fun add(nWise: NWise): String {
        val internalId = NWiseList(context.nonSecrets).add(nWise)
        nWiseMap!![internalId] = nWise
        return internalId
    }

    fun resolveNWiseId(senderVerkeyBase58: String?): String? {
        val myInfos = NWiseList(context.nonSecrets).nWiseInfoList
        val myInternalIds: MutableList<String?> = ArrayList()
        for (info in myInfos) myInternalIds.add(info.internalId)
        for ((key, value) in nWiseMap!!) {
            if (value.currentParticipantsVerkeysBase58.contains(senderVerkeyBase58) && myInternalIds.contains(
                    key
                )
            ) return key
        }
        return null
    }

    fun resolveParticipant(senderVerkeyBase58: String): NWiseParticipant? {
        val internalId = resolveNWiseId(senderVerkeyBase58) ?: return null
        val nWise: NWise? = nWiseMap?.get(internalId)
        return if (nWise != null) nWise.resolveParticipant(senderVerkeyBase58) else null
    }

    fun getParticipants(internalId: String?): List<NWiseParticipant> {
        val nWise = nWiseMap!![internalId]
        return nWise?.participants ?: listOf()
    }

    fun update(internalId: String?): Boolean {
        val nWise = nWiseMap!![internalId]
        return nWise?.fetchFromLedger() ?: false
    }

    fun createInvitation(internalId: String?): Invitation? {
        if (!nWiseMap!!.containsKey(internalId)) return null
        val nWise = nWiseMap!![internalId]
        return nWise!!.createInvitation(context)
    }

    fun acceptInvitation(invitation: Invitation, nickname: String?): String? {
        if (invitation.ledgerType.equals("iota@v1.0")) {
            val nWise: NWise? = IotaNWise.acceptInvitation(invitation, nickname, context)
            if (nWise != null) {
                return add(nWise)
            }
        }
        return null
    }

    fun send(internalId: String?, msg: Message): Boolean {
        return if (!nWiseMap!!.containsKey(internalId)) false else nWiseMap!![internalId]!!.send(
            msg,
            context
        )
    }

    fun leave(internalId: String?, context: Context<*>): Boolean {
        if (!nWiseMap!!.containsKey(internalId)) return false
        val res = nWiseMap!![internalId]!!.leave(context)
        if (res) {
            NWiseList(context.nonSecrets).remove(internalId)
            nWiseMap!!.remove(internalId)
        }
        return res
    }

    fun getNotify(senderVerkeyBase58: String?): Boolean {
        val internalId = resolveNWiseId(senderVerkeyBase58)
        return internalId?.let { update(it) } ?: false
    }

    fun getMe(internalId: String?, context: Context<*>): NWiseParticipant? {
        if (!nWiseMap!!.containsKey(internalId)) return null
        val nWise = nWiseMap!![internalId]
        return nWise!!.resolveParticipant(Base58.encode(nWise.myVerkey))
    }

    init {
        this.context = context
    }
}
