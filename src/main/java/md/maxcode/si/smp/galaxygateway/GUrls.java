/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

import md.maxcode.si.tools.TTSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("gUrls")
public class GUrls {
    @Autowired
    private TTSettings ttSettings;

    public String getParticipantIdentifierUrl() {
        return ttSettings.smpUrl + "/1.0/smp/participantidentifiers" + ".json";
    }

    public String getEndpointsUrl() {
        return ttSettings.smpUrl + "/1.0/smp/endpoints" + ".json";
    }

    public String getMetadataProfilesUrl() {
        return ttSettings.smpUrl + "/1.0/smp/metadataprofiles" + ".json";
    }
}
