package com.sirius.library.n_wise

import com.sirius.library.n_wise.transactions.*
import com.sirius.library.utils.multibase.Base58.decode
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.JcsEd25519Signature2020LdVerifier





class NWiseStateMachine {
    var created = false
    var label: String? = null
    var genesisCreatorVerkey: ByteArray? = null
    var invitationKeys: MutableMap<String, ByteArray> = HashMap()
    var participants: MutableList<NWiseParticipant> = ArrayList()
    var currentOwner: NWiseParticipant? = null

 

    fun check(jsonObject: JSONObject): Boolean {
        val type: String = jsonObject.optString("type") ?:""
        when (type) {
            "genesisTx" -> return check(GenesisTx(jsonObject))
            "addParticipantTx" -> return check(AddParticipantTx(jsonObject))
            "invitationTx" -> return check(InvitationTx(jsonObject))
            "removeParticipantTx" -> return check(RemoveParticipantTx(jsonObject))
            "newOwnerTx" -> return check(NewOwnerTx(jsonObject))
        }
        return true
    }

    fun check(tx: GenesisTx): Boolean {
        return if (created) false else check(tx, tx.creatorVerkey)
    }

    private fun check(o: JSONObject, verkey: ByteArray): Boolean {
        val verifier = JcsEd25519Signature2020LdVerifier(verkey)
        return verifier.verify(o)
    }

    fun check(tx: AddParticipantTx): Boolean {
        if (!tx.has("proof")) return false
        val proof: JSONObject? = tx.getJSONObject("proof")
        val verificationMethod: String? = proof?.optString("verificationMethod")
        if (invitationKeys.containsKey(verificationMethod)) {
            val verkey = invitationKeys[verificationMethod]
            return check(tx, verkey?: ByteArray(0))
        }
        val verkey = getVerificationMethodPublicKey(verificationMethod?:"") ?: return false
        return check(tx, verkey)
    }

    fun check(tx: InvitationTx): Boolean {
        if (!tx.has("proof")) return false
        val proof: JSONObject? = tx.getJSONObject("proof")
        val verificationMethod: String? = proof?.optString("verificationMethod")
        val verkey = getVerificationMethodPublicKey(verificationMethod?:"") ?: return false
        return check(tx, verkey)
    }

    fun check(tx: RemoveParticipantTx): Boolean {
        if (!tx.has("proof")) return false
        val proof: JSONObject? = tx.getJSONObject("proof")
        val verificationMethod: String? = proof?.optString("verificationMethod")
        val verkey = getVerificationMethodPublicKey(verificationMethod?:"") ?: return false
        val signer = resolveParticipant(verkey)
        return if (signer!!.did == tx.did || signer.did == currentOwner!!.did) check(
            tx,
            verkey
        ) else false
    }

    fun check(tx: NewOwnerTx): Boolean {
        if (!tx.has("proof")) return false
        val proof: JSONObject? = tx.getJSONObject("proof")
        val verificationMethod: String? = proof?.optString("verificationMethod")
        val verkey = getVerificationMethodPublicKey(verificationMethod?:"") ?: return false
        val signer = resolveParticipant(verkey)
        return if (signer!!.did != currentOwner!!.did) false else check(tx, verkey)
    }

    private fun getVerificationMethodPublicKey(verificationMethodUri: String): ByteArray? {
        for (p in participants) {
            if (verificationMethodUri.startsWith(p.did!!)) {
                return p.verkey
            }
        }
        return null
    }

    fun append(genesisTx: GenesisTx): Boolean {
        if (!check(genesisTx)) return false
        created = true
        label = genesisTx.label
        val creator = NWiseParticipant()
        creator.nickname = genesisTx.creatorNickname
        creator.did = genesisTx.creatorDid
        creator.didDoc = genesisTx.creatorDidDoc
        participants.add(creator)
        currentOwner = creator
        genesisCreatorVerkey = decode(
            creator.didDoc?.optJSONArray("publicKey")?.getJSONObject(0)?.getString("publicKeyBase58")?:""
        )
        return true
    }

    fun append(tx: AddParticipantTx): Boolean {
        if (!check(tx)) return false
        val participant = NWiseParticipant()
        participant.nickname = tx.nickname
        participant.did = tx.did
        participant.didDoc = tx.didDoc
        participants.add(participant)
        val proof: JSONObject? = tx.getJSONObject("proof")
        val verificationMethod: String? = proof?.optString("verificationMethod")
        if (invitationKeys.containsKey(verificationMethod)) invitationKeys.remove(verificationMethod)
        return true
    }

    fun append(tx: InvitationTx): Boolean {
        if (!check(tx)) return false
        val keys: List<Pair<String, ByteArray>> = tx.publicKeys
        for ((first, second) in keys) {
            invitationKeys[first] = second
        }
        return true
    }

    fun append(tx: UpdateMetadataTx?): Boolean {
        TODO("append UpdateMetadataTx")
    }

    fun append(tx: UpdateParticipantTx?): Boolean {
        TODO("append UpdateParticipantTx")
    }

    fun append(tx: RemoveParticipantTx): Boolean {
        if (!check(tx)) return false
        participants.removeAll {
            it.did == tx.did
        }
        return true
    }

    fun append(tx: NewOwnerTx): Boolean {
        if (!check(tx)) return false
        for (p in participants) {
            if (p.did == tx.did) {
                currentOwner = p
                return true
            }
        }
        return false
    }

    fun append(jsonObject: JSONObject): Boolean {
        val type: String = jsonObject.optString("type") ?:""
        when (type) {
            "genesisTx" -> return append(GenesisTx(jsonObject))
            "addParticipantTx" -> return append(AddParticipantTx(jsonObject))
            "removeParticipantTx" -> return append(RemoveParticipantTx(jsonObject))
            "invitationTx" -> return append(InvitationTx(jsonObject))
            "newOwnerTx" -> return append(NewOwnerTx(jsonObject))
        }
        return false
    }

    fun resolveNickname(verkey: ByteArray?): String? {
        for (p in participants) {
            if (p.verkey.contentEquals(verkey)) return p.nickname
        }
        return null
    }

    fun resolveParticipant(verkey: ByteArray?): NWiseParticipant? {
        for (p in participants) {
            if (p.verkey.contentEquals(verkey)) return p
        }
        return null
    }

    fun resolveDid(verkey: ByteArray?): String? {
        for (p in participants) {
            if (p.verkey.contentEquals(verkey)) return p.did
        }
        return null
    }
}
