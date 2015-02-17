/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

import java.io.Serializable;

public class SchemaFile implements Serializable {

    private Long id;
    private String name;

    private String addedTime;
    private String fileName;
    private String extension;
    private Long typeId;
    private Long userId;
    private boolean marked;


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

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime_) {
        addedTime = addedTime_;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName_) {
        fileName = fileName_;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension_) {
        extension = extension_;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId_) {
        typeId = typeId_;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId_) {
        userId = userId_;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(final boolean marked) {
        this.marked = marked;
    }
}
