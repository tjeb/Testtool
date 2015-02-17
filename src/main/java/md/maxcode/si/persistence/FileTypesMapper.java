/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.FileType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FileTypesMapper {

    @Select("SELECT * FROM \"fileTypes\"")
    List<FileType> getAll();

    @Select("SELECT * FROM \"fileTypes\" WHERE \"id\" = #{id}")
    FileType getById(Long id);

    @Select("SELECT * FROM \"fileTypes\" WHERE \"identifier\" = #{identifier}")
    FileType getByIdentifier(String identifier);

    @Select("SELECT * FROM \"fileTypes\" WHERE \"name\" = #{name}")
    FileType getByName(String name);

    List<FileType> getAllWithTheirAttachments();

    FileType getByIdWithAttachments(Long id);

    void insertType(FileType fileType);

    void updateType(FileType fileType);

    void deleteType(Long id);
}