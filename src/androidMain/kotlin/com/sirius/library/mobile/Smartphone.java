/*
package com.sirius.library.mobile.helpers;



import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage;
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.state_machines.Holder;
import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage;
import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.state_machines.Prover;
import com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message;
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation;
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.state_machines.Invitee;
import com.sirius.library.agent.listener.Event;
import com.sirius.library.agent.listener.Listener;
import com.sirius.library.agent.pairwise.Pairwise;
import com.sirius.library.errors.indy_exceptions.DuplicateMasterSecretNameException;
import com.sirius.library.hub.MobileContext;
import com.sirius.library.hub.MobileHub;
import kotlin.Pair;

public class Smartphone {
    MobileHub.Config config;
    MobileContext context = null;
    Pairwise.Me me = null;
    boolean loop = false;
    String networkName;
    String masterSecret = "masterSecret";

    public Smartphone(MobileHub.Config config, String networkName, String genesisPath) {
        MobileContext.Companion.addPool(networkName, genesisPath);
        this.config = config;
        this.networkName = networkName;
    }

    public void start() {
        if (context == null) {
            context = new MobileContext(config);
            context.connectToMediator("Edge Test agent");
            Pair<String, String> didVk = context.getDid().createAndStoreMyDid();
            me = new Pairwise.Me(didVk.first, didVk.second);
            //context.addMediatorKey(me.getVerkey());
            loop = true;
            new Thread(() -> routine()).start();
        }
    }

    public void stop() {
        if (context != null) {
            context.close();
        }
    }

    public void acceptInvitation(Invitation invitation) {
        Invitee invitee = new Invitee(context, me, context.getEndpointWithEmptyRoutingKeys());
        Pairwise pw = invitee.createConnection(invitation, "Edge agent");
        if (pw != null) {
            context.getPairwiseList().ensureExists(pw);
        }
    }

    protected void routine() {
        try {
            context.getAnonCreds().proverCreateMasterSecret(masterSecret);
        } catch (DuplicateMasterSecretNameException e) {
            e.printStackTrace();
        }
        Listener listener = context.subscribe();
        try {
            while (loop) {
                Event event = listener.getOne().get();
                if (event.message() instanceof OfferCredentialMessage && event.getPairwise() != null) {
                    OfferCredentialMessage offer = (OfferCredentialMessage) event.message();
                    Holder holder = new Holder(context, event.getPairwise(), masterSecret);
                    Pair<Boolean, String> res = holder.accept(offer);
                } else if (event.message() instanceof RequestPresentationMessage && event.getPairwise() != null) {
                    RequestPresentationMessage request = (RequestPresentationMessage) event.message();
                    Prover prover = new Prover(context, event.getPairwise(), masterSecret);
                    prover.prove(request);
                } else if (event.message() instanceof Message && event.getPairwise() != null) {
                    Message message = (Message) event.message();
                    System.out.println("Received new message: " + message.getContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
