/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.FileType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FileTypesMapper {

    @Select("SELECT * FROM file_types")
    List<FileType> getAll();

    @Select("SELECT * FROM file_types WHERE id = #{id}")
    FileType getById(Long id);

    @Select("SELECT * FROM file_types WHERE identifier LIKE '%' || #{identifier} || '%'")
    FileType getByIdentifier(String identifier);

    @Select("SELECT * FROM file_types WHERE name = #{name}")
    FileType getByName(String name);

    List<FileType> getAllWithTheirAttachments();

    FileType getByIdWithAttachments(Long id);

    void insertType(FileType fileType);

    void updateType(FileType fileType);

    void deleteType(Long id);
}
