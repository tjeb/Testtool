/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

import java.io.Serializable;

public class CertificateFile implements Serializable {
    private Long id;
    private Long userId;
    private Long accessPointId;
    private String fileName;
    private String addedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id_) {
        id = id_;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId_) {
        userId = userId_;
    }

    public Long getAccessPointId() {
        return accessPointId;
    }

    public void setAccessPointId(Long accessPointId_) {
        accessPointId = accessPointId_;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName_) {
        fileName = fileName_;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime_) {
        addedTime = addedTime_;
    }
}
