/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

import md.maxcode.si.tools.UtilsComponent;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Map;

public class GMetadataProfile {
    private Integer profileId;
    private String commonName;
    private GIdentifier processIdentifier;
    private GIdentifier documentIdentifier;
    private String base64;

    public GMetadataProfile() {
    }

    public GMetadataProfile(String base64) {
        update(base64);
    }

    public GMetadataProfile(final Map map, final String base64) {
        update(map, base64);
    }

    private void update(final String base64) {
        final String s = new String(Base64.decodeBase64(base64.getBytes()));
        final Map map = (Map) UtilsComponent.instance.jsonToObject(s, Map.class);

        update(map, base64);
    }

    private void update(final Map map, final String base64) {
        profileId = (Integer) map.get("profileId");
        commonName = (String) map.get("commonName");
        processIdentifier = new GIdentifier((Map) map.get("processIdentifier"));
        documentIdentifier = new GIdentifier((Map) map.get("documentIdentifier"));

        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(final String base64) {
        update(base64);
        this.base64 = base64;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(final Integer profileId) {
        this.profileId = profileId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(final String commonName) {
        this.commonName = commonName;
    }

    public GIdentifier getProcessIdentifier() {
        return processIdentifier;
    }

    public void setProcessIdentifier(final GIdentifier processIdentifier) {
        this.processIdentifier = processIdentifier;
    }

    public GIdentifier getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(final GIdentifier documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }
}
