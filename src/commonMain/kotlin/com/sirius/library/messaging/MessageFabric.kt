package com.sirius.library.messaging

import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.*
import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages.*
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Ping
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Pong
import com.sirius.library.agent.aries_rfc.feature_0113_question_answer.messages.AnswerMessage
import com.sirius.library.agent.aries_rfc.feature_0113_question_answer.messages.QuestionMessage
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.*
import com.sirius.library.agent.aries_rfc.feature_0211_mediator_coordination_protocol.*
import com.sirius.library.agent.consensus.simple.messages.*
import com.sirius.library.n_wise.messages.BaseNWiseMessage
import com.sirius.library.n_wise.messages.LedgerUpdateNotify
import kotlin.reflect.KClass

class MessageFabric {
    companion object {


        fun restoreMessageInstance(kClass: KClass<out Message>, payload: String): Message?{
            for (pair in Message.MSG_REGISTRY2) {
                if (pair.first == kClass) {
                    return pair.second.invoke(payload)
                }
            }
            return null
        }

        fun registerAllMessagesClass(){
           Message.registerMessageClass(com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message::class, com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message.PROTOCOL, "message"){
               com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message(it)
           }
            Message.registerMessageClass(Ping::class, Ping.PROTOCOL, "ping"){
                Ping(it)
            }
            Message.registerMessageClass(Pong::class, Pong.PROTOCOL, "ping_response"){
                Pong(it)
            }
            Message.registerMessageClass(Ack::class, Ack.PROTOCOL, "ack"){
                Ack(it)
            }

            Message.registerMessageClass(IssueCredentialMessage::class, BaseIssueCredentialMessage.PROTOCOL, "issue-credential"){
                IssueCredentialMessage(it)
            }

            Message.registerMessageClass(
                IssueProblemReport::class,
                BaseIssueCredentialMessage.PROTOCOL,
                "problem_report"
            ){
                IssueProblemReport(it)
            }

            Message.registerMessageClass(OfferCredentialMessage::class, BaseIssueCredentialMessage.PROTOCOL, "offer-credential"){
                OfferCredentialMessage(it)
            }
            Message.registerMessageClass(ProposeCredentialMessage::class, BaseIssueCredentialMessage.PROTOCOL, "propose-credential"){
                ProposeCredentialMessage(it)
            }

            Message.registerMessageClass(RequestCredentialMessage::class, BaseIssueCredentialMessage.PROTOCOL, "request-credential"){
                RequestCredentialMessage(it)
            }

            Message.registerMessageClass(PresentationMessage::class, BasePresentProofMessage.PROTOCOL, "presentation"){
                PresentationMessage(it)
            }

            Message.registerMessageClass(
                PresentProofProblemReport::class,
                BasePresentProofMessage.PROTOCOL,
                "problem_report"
            ){
                PresentProofProblemReport(it)
            }

            Message.registerMessageClass(RequestPresentationMessage::class, BasePresentProofMessage.PROTOCOL, "request-presentation"){
                RequestPresentationMessage(it)
            }

            Message.registerMessageClass(AnswerMessage::class, "questionanswer", "answer"){
                AnswerMessage(it)
            }

            Message.registerMessageClass(
                QuestionMessage::class,
                "questionanswer",
                "question"
            ){
                QuestionMessage(it)
            }

            Message.registerMessageClass(ConnProblemReport::class, ConnProtocolMessage.PROTOCOL, "problem_report"){
                ConnProblemReport(it)
            }

            Message.registerMessageClass(ConnRequest::class, ConnProtocolMessage.PROTOCOL, "request"){
                ConnRequest(it)
            }

            Message.registerMessageClass(ConnResponse::class, ConnProtocolMessage.PROTOCOL, "response"){
                ConnResponse(it)
            }

            Message.registerMessageClass(Invitation::class, ConnProtocolMessage.PROTOCOL, "invitation"){
                Invitation(it)
            }

            Message.registerMessageClass(KeylistUpdate::class, CoordinateMediationMessage.PROTOCOL, "keylist-update"){
                KeylistUpdate(it)
            }

            Message.registerMessageClass(KeylistUpdateResponse::class, CoordinateMediationMessage.PROTOCOL, "keylist-update-response"){
                KeylistUpdateResponse(it)
            }

            Message.registerMessageClass(MediateDeny::class, CoordinateMediationMessage.PROTOCOL, "mediate-deny"){
                MediateDeny(it)
            }
            Message.registerMessageClass(MediateGrant::class, CoordinateMediationMessage.PROTOCOL, "mediate-grant"){
                MediateGrant(it)
            }

            Message.registerMessageClass(MediateRequest::class, CoordinateMediationMessage.PROTOCOL, "mediate-request"){
                MediateRequest(it)
            }

            Message.registerMessageClass(
                BaseInitLedgerMessage::class,
                SimpleConsensusMessage.PROTOCOL,
                "initialize"
            ){
                BaseInitLedgerMessage(it)
            }

            Message.registerMessageClass(BaseTransactionsMessage::class, SimpleConsensusMessage.PROTOCOL, "stage"){
                BaseTransactionsMessage(it)
            }

            Message.registerMessageClass(
                CommitTransactionsMessage::class,
                SimpleConsensusMessage.PROTOCOL,
                "stage-commit"
            ){
                CommitTransactionsMessage(it)
            }

            Message.registerMessageClass(
                InitRequestLedgerMessage::class,
                SimpleConsensusMessage.PROTOCOL,
                "initialize-request"
            ){
                InitRequestLedgerMessage(it)
            }

            Message.registerMessageClass(
                InitResponseLedgerMessage::class,
                SimpleConsensusMessage.PROTOCOL,
                "initialize-response"
            ){
                InitResponseLedgerMessage(it)
            }

            Message.registerMessageClass(
                PostCommitTransactionsMessage::class,
                SimpleConsensusMessage.PROTOCOL,
                "stage-post-commit"
            ){
                PostCommitTransactionsMessage(it)
            }

            Message.registerMessageClass(
                PreCommitTransactionsMessage::class,
                SimpleConsensusMessage.PROTOCOL,
                "stage-pre-commit"
            ){
                PreCommitTransactionsMessage(it)
            }

            Message.registerMessageClass(
                ProposeTransactionsMessage::class,
                SimpleConsensusMessage.PROTOCOL,
                "stage-propose"
            ){
                ProposeTransactionsMessage(it)
            }

            Message.registerMessageClass(
                SimpleConsensusProblemReport::class,
                SimpleConsensusMessage.PROTOCOL,
                "problem_report"
            ){
                SimpleConsensusProblemReport(it)
            }

            Message.registerMessageClass(
                PresentationAck::class,
                BasePresentProofMessage.PROTOCOL,
                "ack"
            ){
                PresentationAck(it)
            }

            Message.registerMessageClass(
                com.sirius.library.n_wise.messages.Invitation::class,
                BaseNWiseMessage.PROTOCOL,
                "invitation"
            ){
                com.sirius.library.n_wise.messages.Invitation(it)
            }

            Message.registerMessageClass(
                LedgerUpdateNotify::class,
                BaseNWiseMessage.PROTOCOL,
                "ledger-update-notify"
            ){
                LedgerUpdateNotify(it)
            }


        }
    }
}