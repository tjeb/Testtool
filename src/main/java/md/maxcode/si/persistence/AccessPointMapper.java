/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.AccessPoint;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccessPointMapper {

    void insert(AccessPoint item);

    @Select("SELECT * FROM access_points WHERE userId = #{id}")
    List<AccessPoint> getAllByUserId(Long id);

    @Select("SELECT * FROM access_points WHERE userId = #{id} ORDER BY addedTime DESC LIMIT 5")
    List<AccessPoint> getLatestByUserId(Long id);

    @Select("SELECT * FROM access_points WHERE id = #{id} AND userId = #{userId}")
    AccessPoint getById(@Param("id") final Long id, @Param("userId") final Long userId);

    void remove(@Param("id") final Long id, @Param("userId") final Long userId);

    void update(AccessPoint accessPoint_);

    void setUsedForGalaxyGateway(AccessPoint accessPoint_);

    @Select("SELECT * FROM access_points WHERE name = #{name} AND userId = #{userId}")
    AccessPoint getByNameAndUserId(@Param(value = "name") String name, @Param(value = "userId") Long userId);


}
