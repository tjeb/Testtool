/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

public class ScheduledDocumentSending {

    private Long apConfigId;
    private Long fileId;
    private String senderPeppolId;
    private String recipientPeppolId;

    public Long getApConfigId() {
        return apConfigId;
    }

    public void setApConfigId(final Long apConfigId_) {
        apConfigId = apConfigId_;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(final Long fileId_) {
        fileId = fileId_;
    }

    public String getSenderPeppolId() {
        return senderPeppolId;
    }

    public void setSenderPeppolId(final String senderPeppolId_) {
        senderPeppolId = senderPeppolId_;
    }

    public String getRecipientPeppolId() {
        return recipientPeppolId;
    }

    public void setRecipientPeppolId(final String recipientPeppolId_) {
        recipientPeppolId = recipientPeppolId_;
    }
}
