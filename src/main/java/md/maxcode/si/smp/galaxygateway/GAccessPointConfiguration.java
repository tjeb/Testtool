/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

import java.util.List;

public class GAccessPointConfiguration {
    private int endpointId; // 21
    private List<Integer> metadataProfileIds; // ["1", "2"]

    public int getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(final int endpointId) {
        this.endpointId = endpointId;
    }

    public List<Integer> getMetadataProfileIds() {
        return metadataProfileIds;
    }

    public void setMetadataProfileIds(final List<Integer> metadataProfileIds) {
        this.metadataProfileIds = metadataProfileIds;
    }
}
