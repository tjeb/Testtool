/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.UserFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserFileMapper {

    @Select("SELECT * FROM \"userFiles\" WHERE \"id\" = #{id} AND  \"userId\" = #{userId}")
    UserFile getById(@Param("id") Long id, @Param("userId") Long userId);

    @Select("SELECT * FROM \"userFiles\" WHERE \"id\" = #{id} AND \"userId\" = #{userId}")
    UserFile getByIdAndUserId(@Param("id") final Long id, @Param("userId") final Long userId);

    @Select("SELECT \"fileName\" FROM \"userFiles\" WHERE \"id\" = #{id}")
    String getFileNameById(Long id);

    @Select("SELECT * FROM \"userFiles\" WHERE \"userId\" = #{id} ORDER BY \"addedTime\" DESC")
    List<UserFile> getByUserId(Long id);

    @Select("SELECT * FROM \"userFiles\" WHERE \"userId\" = #{id} ORDER BY \"addedTime\" DESC LIMIT 5")
    List<UserFile> getLatestByUserId(Long id);

    void updateFile(UserFile file);

    void insertFile(UserFile file);

    void deleteFile(Long id);

    void deleteFiles(List<Long> ids);

    @Select("SELECT * FROM \"userFiles\" WHERE \"typeId\" = #{id}")
    List<UserFile> getAllBytTypeId(Long id);

    @Select("SELECT \"addedTime\" FROM \"userFiles\" WHERE \"id\" = #{id}")
    String getAddedTime(final Long id);
}