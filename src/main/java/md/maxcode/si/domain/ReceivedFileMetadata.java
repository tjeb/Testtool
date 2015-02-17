/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class ReceivedFileMetadata {
    private String timeStamp;
    private String messageFileName;
    private String messageIdentifier;
    private String channelIdentifier;
    private String recipientIdentifier;
    private String senderIdentifier;
    private String documentIdentifier;
    private String processIdentifier;
    private String remote;
    private String apPrincipal;
    private Long id;


    public ReceivedFileMetadata(JSONObject outerJsonObject, String filePath, String channelId) throws JSONException {
        JSONObject jsonObject = outerJsonObject.getJSONObject("PeppolMessageMetaData");

        timeStamp = jsonObject.getString("receivedTimeStamp");
        //Oxalis AS2 protocol now uses .xml
        messageFileName = filePath.replace(".txt", ".xml");
        messageIdentifier = jsonObject.getString("transmissionId");
        recipientIdentifier = jsonObject.getString("recipientId");
        senderIdentifier = jsonObject.getString("senderId");
        documentIdentifier = jsonObject.getString("documentTypeIdentifier");
        processIdentifier = jsonObject.getString("profileTypeIdentifier");
        remote = jsonObject.getString("userAgent");
        apPrincipal = jsonObject.getString("sendingAccessPointPrincipal");
        this.channelIdentifier = channelId;
    }

    public ReceivedFileMetadata(Properties properties, String filePath) {
        timeStamp = properties.getProperty("receivedTimeStamp");
        messageFileName = filePath;
        messageIdentifier = properties.getProperty("transmissionId");
        recipientIdentifier = properties.getProperty("recipientId");
        senderIdentifier = properties.getProperty("senderId");
        documentIdentifier = properties.getProperty("documentTypeIdentifier");
        processIdentifier = properties.getProperty("profileTypeIdentifier");
        remote = properties.getProperty("Remote");
        apPrincipal = properties.getProperty("sendingAccessPointPrincipal");
    }

    public Date getTimestampAsDate() throws ParseException {
        String timeStampx = timeStamp.replace("'T'", " ");
        int length = timeStampx.length();
        String zone = timeStampx.substring(length - 3, length);
        String dateTime = timeStampx.substring(0, length - 3);
        TimeZone timeZone = TimeZone.getTimeZone("GMT" + zone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.parse(dateTime);
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(final String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageFileName() {
        return messageFileName;
    }

    public void setMessageFileName(final String messageFileName) {
        this.messageFileName = messageFileName;
    }

    public String getMessageIdentifier() {
        return messageIdentifier;
    }

    public void setMessageIdentifier(final String messageIdentifier) {
        this.messageIdentifier = messageIdentifier;
    }

    public String getChannelIdentifier() {
        return channelIdentifier;
    }

    public void setChannelIdentifier(final String channelIdentifier) {
        this.channelIdentifier = channelIdentifier;
    }

    public String getRecipientIdentifier() {
        return recipientIdentifier;
    }

    public void setRecipientIdentifier(final String recipientIdentifier) {
        this.recipientIdentifier = recipientIdentifier;
    }

    public String getSenderIdentifier() {
        return senderIdentifier;
    }

    public void setSenderIdentifier(final String senderIdentifier) {
        this.senderIdentifier = senderIdentifier;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(final String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public String getProcessIdentifier() {
        return processIdentifier;
    }

    public void setProcessIdentifier(final String processIdentifier) {
        this.processIdentifier = processIdentifier;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(final String remote) {
        this.remote = remote;
    }

    public String getApPrincipal() {
        return apPrincipal;
    }

    public void setApPrincipal(final String apPrincipal) {
        this.apPrincipal = apPrincipal;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long geId() {
        return id;
    }
}

