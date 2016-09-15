/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserFile implements Serializable {
    private Long id;
    private String name;
    private String fileName;
    private Long size;
    private Long typeId;
    private String extension;
    private String addedTime;
    private Long userId;
    private Boolean validated;
    private String validationInfo;
    private Long receivedMetadataId;

    public String getValidationInfo() {
        return validationInfo;
    }

    public void setValidationInfo(String validationInfo_) {
        validationInfo = validationInfo_;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(final String extension_) {
        extension = extension_;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId_) {
        this.userId = userId_;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size_) {
        size = size_;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName_) {
        fileName = fileName_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name_) {
        name = name_;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id_) {
        id = id_;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId_) {
        typeId = typeId_;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime_) {
        addedTime = addedTime_;
    }

    public Date getAddedTimeAsDate() throws ParseException {
        // Fix for mysql support; can this not be done directly from the db?
	String dateTime = addedTime;
	TimeZone timeZone = null;
        if (addedTime.indexOf("+") > 0) {
            String[] split = addedTime.split("\\+");
            String zone = "+" + split[1];
            dateTime = split[0];
            timeZone = TimeZone.getTimeZone("GMT" + zone);
        }

        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.parse(dateTime);
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated_) {
        validated = validated_;
    }

    public Long getReceivedMetadataId() {
        return receivedMetadataId;
    }

    public void setReceivedMetadataId(final Long receivedMetadataId) {
        this.receivedMetadataId = receivedMetadataId;
    }
}
