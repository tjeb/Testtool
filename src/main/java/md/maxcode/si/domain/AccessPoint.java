/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

public class AccessPoint {

    private Long id;
    private Long userId;
    // id of the certificate file issued for this AP based on csr request
    private Long fileId;

    private String name;
    private String url;
    private String description;
    private String contactEmail;
    private String technicalUrl;
    private boolean usedForGalaxyGateway;

    public String getName() {
        return name;
    }

    public void setName(final String name_) {
        name = name_;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url_) {
        url = url_;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(final Long fileId_) {
        fileId = fileId_;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id_) {
        id = id_;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId_) {
        userId = userId_;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(final String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getTechnicalUrl() {
        return technicalUrl;
    }

    public void setTechnicalUrl(final String technicalUrl) {
        this.technicalUrl = technicalUrl;
    }

    public boolean isUsedForGalaxyGateway() {
        return usedForGalaxyGateway;
    }

    public void setUsedForGalaxyGateway(final boolean usedForGalaxyGateway) {
        this.usedForGalaxyGateway = usedForGalaxyGateway;
    }
}
