/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

import java.util.ArrayList;
import java.util.List;

public class GEndpointRequest {
    private List<GEndPoint> endpoints = new ArrayList<GEndPoint>();

    public List<GEndPoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(final List<GEndPoint> endpoints) {
        this.endpoints = endpoints;
    }
}
