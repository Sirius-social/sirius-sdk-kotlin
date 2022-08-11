package com.sirius.library.did_doc

import com.sirius.library.agent.wallet.abstract_wallet.AbstractCrypto
import com.sirius.library.encryption.IndyWalletSigner
import com.sirius.library.hub.Context
import com.sirius.library.utils.Base58.decode
import com.sirius.library.utils.Base58.encode
import com.sirius.library.utils.IotaUtils
import com.sirius.library.utils.IotaUtils.node
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject
import com.sirius.library.utils.Logger
import org.iota.client.Client
import org.iota.client.Message
import org.iota.client.MessageId


class IotaPublicDidDoc : PublicDidDoc {
    var log = Logger.getLogger(IotaPublicDidDoc::class.simpleName)
    private var meta: JSONObject = JSONObject()
    var publicKey: ByteArray
    var tag: String
    var previousMessageId = ""

    constructor(crypto: AbstractCrypto) {
        publicKey = decode(crypto.createKey()!!)
        tag = IotaUtils.generateTag(publicKey)
        payload.put("id", "did:iota:$tag")
    }

    private constructor(msg: Message) {
        val obj = JSONObject(String(msg.payload().get().asIndexation().data()))
        payload = obj.optJSONObject("doc")
        meta = obj.optJSONObject("meta")
        previousMessageId = msg.id().toString()
        tag = payload.optString("id")!!.substring("did:iota:".length)
        val verificationMethod: JSONObject? = getVerificationMethod(obj)
        publicKey = Multibase.decode(verificationMethod.optString("publicKeyMultibase"))
    }

    val didDoc: JSONObject
        get() = payload

    override fun submitToLedger(context: Context<*>): Boolean {
        val o: JSONObject = generateIntegrationMessage(context.crypto) ?: return false
        val iota: Client = node()
        return try {
            val message: Message = iota.message().withIndexString(tag)
                .withData(o.toString().getBytes(StandardCharsets.UTF_8)).finish()
            previousMessageId = message.id().toString()
            saveToWallet(context.nonSecrets)
            true
        } catch (ex: Exception) {
            false
        }
    }

    private fun generateIntegrationMessage(crypto: AbstractCrypto): JSONObject? {
        val byteSigner: ByteSigner = IndyWalletSigner(crypto, encode(publicKey))
        val capabilityInvocation = JSONArray()
        capabilityInvocation.put(
            JSONObject()
                .put("id", payload.optString("id") + "#sign-0")
                .put("controller", payload.optString("id"))
                .put("type", "Ed25519VerificationKey2018")
                .put("publicKeyMultibase", Multibase.encode(Multibase.Base.Base58BTC, publicKey))
        )
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'")
        payload.put("capabilityInvocation", capabilityInvocation)
        if (previousMessageId.isEmpty()) {
            meta.put("created", dateFormat.format(Date()))
        } else {
            meta.put("previousMessageId", previousMessageId)
        }
        meta.put("updated", dateFormat.format(Date()))
        val ldSigner: LdSigner = JcsEd25519Signature2020LdSigner(byteSigner)
        ldSigner.setVerificationMethod(URI.create(payload.optString("id") + "#sign-0"))
        val resMsg: JSONObject = JSONObject().put("doc", payload).put("meta", meta)
        val jsonLdObject: JsonLDObject = JsonLDObject.fromJson(resMsg.toString())
        var proof: JSONObject? = null
        try {
            proof = JSONObject(ldSigner.sign(jsonLdObject).toJson())
            resMsg.put("proof", proof)
            return resMsg
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        fun load(did: String): IotaPublicDidDoc? {
            val msg: Message? = loadLastValidIntegrationMessage(did)
            return if (msg?.payload() == null) null else IotaPublicDidDoc(msg)
        }

        private fun loadLastValidIntegrationMessage(did: String): Message? {
            return try {
                val tag = tagFromId(did)
                val fetchedMessageIds: Array<MessageId> = node().message.indexString(tag)
                val map: HashMap<String, MutableList<Message>> = HashMap()
                for (msgId in fetchedMessageIds) {
                    val msg: Message = node().message.data(msgId)
                    if (msg.payload()!=null) {
                        val obj = JSONObject(String(msg.payload().get().asIndexation().data()))
                        val previousMessageId: String =
                            obj.optJSONObject("meta").optString("previousMessageId", "")
                        if (!map.containsKey(previousMessageId)) map[previousMessageId] =
                           listOf(msg) else map[previousMessageId]!!
                            .add(msg)
                    }
                }
                if (!map.containsKey("")) return null
                var prevMessageId = ""
                var prevMessage: Message? = null
                while (!map.isEmpty()) {
                    if (map.containsKey(prevMessageId)) {
                        val finalPrevMessage: Message? = prevMessage
                        val list: List<Message> = map[prevMessageId].stream().filter { m ->
                            checkMessage(
                                m,
                                finalPrevMessage
                            )
                        }.sorted(IotaUtils.msgComparator).collect(Collectors.toList())
                        if (list.isEmpty()) {
                            return prevMessage
                        } else {
                            map.remove(prevMessageId)
                            prevMessage = list[list.size - 1]
                            prevMessageId = prevMessage.id().toString()
                        }
                    } else {
                        break
                    }
                }
                prevMessage
            } catch (ex: Exception) {
                null
            }
        }

        private fun getVerificationMethod(
            obj: JSONObject,
            verificationMethodId: String?
        ): JSONObject? {
            val verificationMethods: JSONArray =
                obj.optJSONObject("doc")?.optJSONArray("capabilityInvocation")?: JSONArray()
            for (o in verificationMethods) {
                val verificationMethod: JSONObject = o as JSONObject
                if (verificationMethod.optString("id").equals(verificationMethodId)) {
                    return verificationMethod
                }
            }
            return null
        }

        private fun getVerificationMethod(obj: JSONObject): JSONObject? {
            val verificationMethodId: String? =
                obj.optJSONObject("proof")?.optString("verificationMethod")
            return getVerificationMethod(obj, verificationMethodId)
        }

        private fun tagFromId(id: String): String {
            return if (id.startsWith("did:iota:")) id.substring("did:iota:".length) else id
        }

        private fun isFirstMessage(jsonMsg: JSONObject): Boolean {
            return jsonMsg.optJSONObject("meta").has("previousMessageId")
        }

        private fun checkFirstMessageTag(jsonMsg: JSONObject): Boolean {
            val verificationMethod: JSONObject = getVerificationMethod(jsonMsg)
                ?: return false
            val pubKeyMultibase: String? = verificationMethod.optString("publicKeyMultibase")
            val tag = tagFromId(jsonMsg.optJSONObject("doc").optString("id"))
            return tag == IotaUtils.generateTag(Multibase.decode(pubKeyMultibase))
        }

        private fun checkMessage(integrationMsg: Message, prevIntegrationMsg: Message?): Boolean {
            var prevIntegrationMsg: Message? = prevIntegrationMsg
            if (prevIntegrationMsg == null) prevIntegrationMsg = integrationMsg
            if (!integrationMsg.payload().isPresent() && !prevIntegrationMsg.payload()
                    .isPresent()
            ) return false
            val integrationMsgJson =
                JSONObject(String(integrationMsg.payload().get().asIndexation().data()))
            val prevIntegrationMsgJson =
                JSONObject(String(prevIntegrationMsg.payload().get().asIndexation().data()))
            if (isFirstMessage(integrationMsgJson) && !checkFirstMessageTag(integrationMsgJson)) return false
            val verificationMethodId: String =
                integrationMsgJson.optJSONObject("proof").optString("verificationMethod")
            val verificationMethod: JSONObject =
                getVerificationMethod(prevIntegrationMsgJson, verificationMethodId)
                    ?: return false
            val pubKeyMultibase: String = verificationMethod.optString("publicKeyMultibase")
            val ldVerifier: LdVerifier<JcsEd25519Signature2020SignatureSuite> =
                JcsEd25519Signature2020LdVerifier(Multibase.decode(pubKeyMultibase))
            val ldObject: JsonLDObject = JsonLDObject.fromJson(integrationMsgJson.toString())
            try {
                return ldVerifier.verify(ldObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}
