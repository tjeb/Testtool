/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GParticipantIdentifier extends GIdentifier {
    private String base64;
    private String name;
    private Boolean smlActivation = true;

    // not mandatory
    private List<GAccessPointConfiguration> accessPointConfigurations = new ArrayList<GAccessPointConfiguration>();

    public GParticipantIdentifier(final Map map) {
        super(map);
    }

    public GParticipantIdentifier() {

    }

    public GParticipantIdentifier(final Map item, final String base64) {
        setName(String.valueOf(item.get("name")));
        setScheme(String.valueOf(item.get("scheme")));
        setValue(String.valueOf(item.get("value")));
        setSmlActivation((Boolean) item.get("smlActivation"));

        this.base64 = base64;

        List configs = (List) item.get("accessPointConfigurations");

        if (configs == null) {
            return;
        }

        for (int i = 0; i < configs.size(); i++) {
            Map map = (Map) configs.get(i);

            GAccessPointConfiguration config = new GAccessPointConfiguration();
            config.setEndpointId((Integer) map.get("endpointId"));

            List metadataProfileIds = (List) map.get("metadataProfileIds");
            Iterator<Integer> iterator = metadataProfileIds.iterator();
            List<Integer> mpids = new ArrayList<>();
            while (iterator.hasNext()) {
                mpids.add(iterator.next());
            }

            config.setMetadataProfileIds(mpids);
            accessPointConfigurations.add(config);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Boolean getSmlActivation() {
        return smlActivation;
    }

    public void setSmlActivation(final Boolean smlActivation) {
        this.smlActivation = smlActivation;
    }

    public List<GAccessPointConfiguration> getAccessPointConfigurations() {
        return accessPointConfigurations;
    }

    public void setAccessPointConfigurations(final List<GAccessPointConfiguration> accessPointConfigurations) {
        this.accessPointConfigurations = accessPointConfigurations;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(final String base64) {
        this.base64 = base64;
    }
}
