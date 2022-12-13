package com.sirius.library.mobile.scenario.impl


import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder
import com.sirius.library.agent.listener.Event
import com.sirius.library.agent.pairwise.Pairwise
import com.sirius.library.errors.indy_exceptions.DuplicateMasterSecretNameException
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.EventTags
import com.sirius.library.mobile.SiriusSDK
import com.sirius.library.mobile.scenario.*
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.sirius.library.mobile.utils.HashUtils
import kotlin.reflect.KClass


abstract class HolderScenario(val eventStorage: EventStorageAbstract) : BaseScenario(),
    EventActionAbstract {



    override fun initMessages(): List<KClass< out Message>> {
        return listOf(OfferCredentialMessage::class)
    }

/*    override fun stop(cause: String) {
        //TODO send problem report*/
    /*getEvent()
    val coprotocol =  CoProtocolP2P(SiriusSDK.getInstance().context, event?.pairwise, protocols(), timeToLiveSec)) {*/

    /* problemReport = IssueProblemReport.builder().setProblemCode(ex.getProblemCode())
         .setExplain(ex.getExplain()).setDocUri(docUri).build()
     log.info("100% - Terminated with error. " + ex.getProblemCode() + " " + ex.getExplain())
     if (ex.isNotify()) coprotocol.send(problemReport)*/

    /*     val problemReport = IssueProblemReport.builder().setExplain(cause).build()

         onScenarioEnd(id,false, cause)
     }*/

    override suspend fun start(event: Event): Pair<Boolean, String?> {
        try {
            //FixMe label not good idea here
            val masterSecretId: String =
                HashUtils.generateHash(SiriusSDK.label?:"")
            SiriusSDK.context?.anonCreds?.proverCreateMasterSecret(masterSecretId)
        } catch (ignored: DuplicateMasterSecretNameException) {
        }
        val pair = EventTransform.eventToPair(event)
        eventStorage.eventStore(pair.second?.getId()?:"", pair, null)
        return Pair(true, "")
    }


    override suspend fun actionStart(
        action: EventAction,
        id: String,
        comment: String?,
        actionListener: EventActionListener?
    ) {
        if (action == EventAction.accept) {
            accept(id, comment, actionListener)
        } else if (action == EventAction.cancel) {
            cancel(id, comment, actionListener)
        }
    }

    suspend fun accept(id: String, comment: String?, eventActionListener: EventActionListener?) {
        eventActionListener?.onActionStart(EventAction.accept, id, comment)

        val locale: String = "en"
        val event = eventStorage.getEvent(id)
        val pairwise  : Pairwise?= PairwiseHelper.getPairwise(event?.first)
        //FixMe Label not good idea here
        val masterSecretId: String =
            HashUtils.generateHash(SiriusSDK.label?:"")
        println("holder masterSecretId="+masterSecretId)
        var  holderMachine : Holder? = null
            if(pairwise!=null){
             holderMachine = SiriusSDK.context?.let { Holder(it, pairwise, masterSecretId) }
        }
        val offer = event?.second as? OfferCredentialMessage
        var error: String? = null
        var result: Pair<Boolean, String?>? =
            Pair(false, error)
        try {
            if(offer!=null){
                result = holderMachine?.accept(offer, comment)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val params = mutableMapOf<String,Any?>()
        params["isAccepted"] = result?.first ?: false
        params["acceptedComment"] = comment
        event?.let {
            eventStorage.eventStore(id, event,  params)
        }
        eventActionListener?.onActionEnd(
            EventAction.accept,
            id,
            comment,
            result?.first ?: false,
            result?.second
        )
    }

    suspend fun cancel(id: String, cause: String?, eventActionListener: EventActionListener?) {
        eventActionListener?.onActionStart(EventAction.cancel, id, cause)
        val event = eventStorage.getEvent(id)
        val pairwise  : Pairwise?= PairwiseHelper.getPairwise(event?.first)
        if(pairwise!=null){
           val holderMachine = SiriusSDK.context?.let { Holder(it, pairwise, null) }
            val offer = event?.second as? OfferCredentialMessage
            offer?.let {     holderMachine?.cancel(offer,"500",cause)}
        }
        event?.let {
            val params = mutableMapOf<String,Any?>()
            params["isAccepted"] =  false
            params["isCanceled"] =  true
            params["canceledCause"] =  cause
            eventStorage.eventStore(id, event,params)
        }
        eventActionListener?.onActionEnd(EventAction.cancel, id, null, false, cause)
    }


    override fun onScenarioStart(id: String,event: Event) {

    }

    override fun onScenarioEnd(id: String,event: Event, success: Boolean, error: String?) {
    }
}