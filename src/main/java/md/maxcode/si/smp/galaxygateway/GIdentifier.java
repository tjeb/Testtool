/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

import java.util.Map;

public class GIdentifier {
    private String scheme;
    private String value;

    public GIdentifier() {
    }

    public GIdentifier(final Map map) {
        scheme = (String) map.get("scheme");
        value = (String) map.get("value");
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(final String scheme) {
        this.scheme = scheme;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
