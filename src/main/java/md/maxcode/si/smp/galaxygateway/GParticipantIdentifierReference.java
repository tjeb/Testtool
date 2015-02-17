/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

public class GParticipantIdentifierReference {
    private String participantIdValue;
    private Integer endpointId;
    private Long userId;
    private long id;

    public String getParticipantIdValue() {
        return participantIdValue;
    }

    public void setParticipantIdValue(final String participantIdValue) {
        this.participantIdValue = participantIdValue;
    }

    public Integer getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(final Integer endpointId) {
        this.endpointId = endpointId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
