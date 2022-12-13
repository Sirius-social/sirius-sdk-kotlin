package com.sirius.library.mobile.scenario.impl


import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.state_machines.Prover
import com.sirius.library.agent.listener.Event
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.SiriusSDK
import com.sirius.library.mobile.scenario.*
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.sirius.library.mobile.utils.HashUtils
import kotlin.reflect.KClass


abstract class ProverScenario(val eventStorage : EventStorageAbstract) : BaseScenario(), EventActionAbstract {

    override fun initMessages(): List<KClass<out Message>> {
        return listOf(RequestPresentationMessage::class)
    }



    override suspend fun start(event: Event): Pair<Boolean, String?> {
        val eventPair = EventTransform.eventToPair(event)
        val id = eventPair.second?.getId()
        eventStorage.eventStore(id!!, eventPair, null)
        return Pair(true, null)
    }

    override fun onScenarioStart(id: String,event: Event) {

    }

    override fun onScenarioEnd(id: String,event: Event,success: Boolean, error: String?) {

    }


    override suspend fun actionStart(action: EventAction, id: String, comment: String?, actionListener: EventActionListener?) {
        if (action == EventAction.accept) {
            accept(id, comment,actionListener)
        } else if (action == EventAction.cancel) {
            cancel(id, comment,actionListener)
        }
    }

    suspend fun accept(id: String, comment: String?, actionListener: EventActionListener?, poolName : String? =null) {
        actionListener?.onActionStart(EventAction.accept, id, comment)
        val event = eventStorage.getEvent(id)
        val requestPresentation = event?.second as? RequestPresentationMessage
        val ttl = 60
        val pairwise = PairwiseHelper.getPairwise(event?.first)
            //FIXMe Label not good idea here
        val masterSecretId: String =
            HashUtils.generateHash(SiriusSDK.label?:"")
        println("prover masterSecretId="+masterSecretId)
        // val proverLedger: Ledger? = SiriusSDK.getInstance().context.getLedgers().get("default")
        // proverLedger?.let {
        var machine :Prover? =null
            if(pairwise!=null){
            machine = SiriusSDK.context?.let { Prover(it, pairwise, masterSecretId,poolName) }
        }
        var isProved = false
        try{
            if(requestPresentation!=null){
                isProved = machine?.prove(requestPresentation) ?: false
            }

        }catch (e : Exception){
            e.printStackTrace()
        }
        val text = machine?.problemReport
        event?.let {
            val params = mutableMapOf<String,Any?>()
            params["isAccepted"] =  isProved
            params["acceptedComment"] =  comment
            params["isCanceled"] =  text != null
            params["canceledCause"] =  text?.explain
            eventStorage.eventStore(id, event, params)
        }
        actionListener?.onActionEnd(EventAction.accept, id, comment, isProved, text?.explain)
    }

    suspend fun cancel(id: String, cause: String?,actionListener: EventActionListener?) {
        actionListener?.onActionStart(EventAction.cancel, id, cause)
        val event = eventStorage.getEvent(id)
        val pairwise = PairwiseHelper.getPairwise(event?.first)
        if(pairwise!=null){
            val requestPresentation = event?.second as? RequestPresentationMessage
           val machine = SiriusSDK.context?.let { Prover(it, pairwise, null,null) }
            machine?.cancel(requestPresentation,"500",cause)
        }
        event?.let {
            val params = mutableMapOf<String,Any?>()
            params["isAccepted"] =  false
            params["isCanceled"] = true
            params["canceledCause"] =  cause
            eventStorage.eventStore(id, event, params)
        }
        actionListener?.onActionEnd(EventAction.accept, id, null, false, cause)
    }
}