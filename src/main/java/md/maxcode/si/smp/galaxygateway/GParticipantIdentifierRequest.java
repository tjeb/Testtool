/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

import java.util.ArrayList;
import java.util.List;

public class GParticipantIdentifierRequest {
    private List<GParticipantIdentifier> participantIdentifiers = new ArrayList<GParticipantIdentifier>();

    public List<GParticipantIdentifier> getParticipantIdentifiers() {
        return participantIdentifiers;
    }

    public void setParticipantIdentifiers(final List<GParticipantIdentifier> participantIdentifiers) {
        this.participantIdentifiers = participantIdentifiers;
    }
}
