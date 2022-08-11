package com.sirius.library.agent.aries_rfc

import com.sirius.library.agent.connections.Endpoint
import com.sirius.library.hub.Context
import com.sirius.library.utils.JSONArray
import com.sirius.library.utils.JSONObject
import kotlin.jvm.JvmOverloads

open class DidDoc {
    var payload: JSONObject = JSONObject()

    fun getPayloadi(): JSONObject {
        return payload
    }

    constructor(){

    }

    constructor(payload: JSONObject) {
        this.payload = payload
    }

    open fun getDid(): String? {
        return payload.optString("id")
    }

    @JvmOverloads
    fun extractService(highPriority: Boolean = true, type: String = "IndyAgent"): JSONObject? {
        val services: JSONArray? = payload.optJSONArray("service")
        if (services != null) {
            var ret: JSONObject? = null
            for (serviceObj in services) {
                val service: JSONObject = serviceObj as JSONObject
                if (service.optString("type") != type) continue
                if (ret == null) {
                    ret = service
                } else {
                    if (highPriority) {
                        if (service.optInt("priority", 0) ?: 0 > ret.optInt("priority", 0) ?: 0) {
                            ret = service
                        }
                    }
                }
            }
            return ret
        }
        return null
    }

    open fun addService(type: String?, endpoint: Endpoint): JSONObject? {
        val service = JSONObject()
        var services = payload.optJSONArray("service")
        if (services == null) {
            services = JSONArray()
            payload.put("service", services)
        }
        service.put("id", getDid().toString() + "#" + services.length())
        service.put("type", type)
        service.put("serviceEndpoint", endpoint.address)
        if (!endpoint.routingKeys.isEmpty()) {
            service.put("routingKeys", endpoint.routingKeys)
        }
        services.put(service)
        return service
    }

    open fun addAgentServices(context: Context<*>) {
        val endpoints = context.endpoints.orEmpty()
        for (e in endpoints) {
            addService("DIDCommMessaging", e)
        }
    }

    companion object {
        const val DID = "did"
        const val DID_DOC = "did_doc"
        const val VCX_DID = "DID"
        const val VCX_DID_DOC = "DIDDoc"
    }

    init {
        this.payload = payload
    }
}
