/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.smp.galaxygateway.GMetadataProfile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GalaxyMetadataProfileMapper {

    void insert(@Param("profile") GMetadataProfile profile, @Param("fileTypeId") Long fileTypeId);

    void removeByFileTypeId(Long fileTypeId);

    void removeByProfileId(Long profileId);

    @Select("select profileId from galaxy_metadata_profiles")
    List<Integer> getAllProfileIds();
}
