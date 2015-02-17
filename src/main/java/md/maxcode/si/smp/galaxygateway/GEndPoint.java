/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.smp.galaxygateway;

public class GEndPoint {
    private int endpointId;
    private String transportProfile = "busdox-transport-start";
    private String endpointAddress;
    private Boolean requireBusinessLevelSignature = false;
    private int minimumAuthenticationLevel = 1;
    private String serviceActivationDate;
    private String serviceExpirationDate;
    private String certificate;
    private String serviceDescription;
    private String technicalContactUrl;
    private String technicalInformationUrl;

    public int getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(final int endpointId) {
        this.endpointId = endpointId;
    }

    public String getTransportProfile() {
        return transportProfile;
    }

    public void setTransportProfile(final String transportProfile) {
        this.transportProfile = transportProfile;
    }

    public String getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(final String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }

    public Boolean getRequireBusinessLevelSignature() {
        return requireBusinessLevelSignature;
    }

    public void setRequireBusinessLevelSignature(final Boolean requireBusinessLevelSignature) {
        this.requireBusinessLevelSignature = requireBusinessLevelSignature;
    }

    public int getMinimumAuthenticationLevel() {
        return minimumAuthenticationLevel;
    }

    public void setMinimumAuthenticationLevel(final int minimumAuthenticationLevel) {
        this.minimumAuthenticationLevel = minimumAuthenticationLevel;
    }

    public String getServiceActivationDate() {
        return serviceActivationDate;
    }

    public void setServiceActivationDate(final String serviceActivationDate) {
        this.serviceActivationDate = serviceActivationDate;
    }

    public String getServiceExpirationDate() {
        return serviceExpirationDate;
    }

    public void setServiceExpirationDate(final String serviceExpirationDate) {
        this.serviceExpirationDate = serviceExpirationDate;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(final String certificate) {
        this.certificate = certificate;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(final String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getTechnicalContactUrl() {
        return technicalContactUrl;
    }

    public void setTechnicalContactUrl(final String technicalContactUrl) {
        this.technicalContactUrl = technicalContactUrl;
    }

    public String getTechnicalInformationUrl() {
        return technicalInformationUrl;
    }

    public void setTechnicalInformationUrl(final String technicalInformationUrl) {
        this.technicalInformationUrl = technicalInformationUrl;
    }
}
