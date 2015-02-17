/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

import java.io.Serializable;
import java.util.List;

public class FileType implements Serializable {
    private Long id;
    private String name;
    private List<SchemaFile> attachedFiles;
    private String identifier;
    private String addedTime;

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(final String addedTime_) {
        addedTime = addedTime_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name_) {
        this.name = name_;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id_) {
        this.id = id_;
    }

    public List<SchemaFile> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<SchemaFile> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier_) {
        identifier = identifier_;
    }
}
