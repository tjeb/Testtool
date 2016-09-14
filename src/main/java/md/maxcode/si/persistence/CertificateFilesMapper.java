/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.CertificateFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CertificateFilesMapper {
    @Select("SELECT * FROM certificate_files WHERE id = #{id} AND userId = #{userId}")
    CertificateFile getById(@Param("id") final Long id, @Param("userId") final Long userId);

    @Select("SELECT fileName FROM certificate_files WHERE id = #{id}")
    String getFileNameById(final Long id);

    @Select("SELECT * FROM certificate_files WHERE userId = #{id} ORDER BY addedTime DESC")
    List<CertificateFile> getByUserId(final Long id);

    @Select("SELECT * FROM certificate_files WHERE userId = #{id} ORDER BY addedTime DESC LIMIT 5")
    List<CertificateFile> getLatestByUserId(final Long id);

    void insertFile(final CertificateFile file);

    void deleteFile(final Long id);

    void deleteFiles(final List<Long> ids);
}
