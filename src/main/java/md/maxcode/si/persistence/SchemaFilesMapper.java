/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.SchemaFile;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SchemaFilesMapper {

    @Select("SELECT * FROM \"schemaFiles\" WHERE \"id\" = #{id}")
    SchemaFile getById(Long id);

    @Select("SELECT * FROM \"schemaFiles\" WHERE \"typeId\" = #{typeId} AND marked=TRUE")
    SchemaFile getMarkedSchemaFile(Long typeId);

    @Select("SELECT \"fileName\" FROM \"schemaFiles\" WHERE \"id\" = #{id}")
    String getFileNameById(Long id);

    @Select("SELECT * FROM \"schemaFiles\" WHERE \"userId\" = #{id} ORDER BY \"addedTime\" DESC")
    List<SchemaFile> getByUserId(Long id);

    @Select("SELECT * FROM \"schemaFiles\" WHERE \"userId\" = #{id} ORDER BY \"addedTime\" DESC LIMIT 5")
    List<SchemaFile> getLatestByUserId(Long id);

    @Select("SELECT * FROM \"schemaFiles\" WHERE ORDER BY \"addedTime\" DESC")
    List<SchemaFile> getAllSchemas();

    void updateFile(SchemaFile file);

    void insertFile(SchemaFile file);

    void deleteFile(Long id);

    void deleteFiles(List<Long> ids);

    @Select("SELECT * FROM \"schemaFiles\" WHERE \"typeId\" = #{id}")
    List<SchemaFile> getAllBytTypeId(Long id);
}
