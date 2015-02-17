/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.ReceivedFileMetadata;
import md.maxcode.si.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReceivedMetadataMapper {

    @Select("SELECT * FROM \"receivedMetadata\" WHERE \"id\"=#{id}")
    List<User> getById(final Long id);

    void write(ReceivedFileMetadata fileMetadata);

    void remove(Long id);

}