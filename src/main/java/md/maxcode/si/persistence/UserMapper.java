/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.domain.User;
import md.maxcode.si.security.Role;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("SELECT * FROM users ORDER BY id ASC")
    List<User> getAll();

    @Select("SELECT * FROM users ORDER BY id ASC")
    List<User> getAllNonEditable();

    @Select("SELECT * FROM user_roles WHERE userId = #{id}")
    List<Role> getRolesById(Long id);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User getByEmail(String email);

    User getById(Long id);

    User getByUsername(String username);

    User getByIdentifier(String identifier);

    Long getIdByUsername(String username);

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(Long userId);

    void deleteRoleUser(Long userId);

    void insertRole(@Param("userId") Long userId, @Param("authority") String authority);

    String getTempPassword(final Long userId);

    void deleteTempPassword(Long userId);

    void writeTempPassword(@Param("userId") Long userId, @Param("password") String password);

    @Options(flushCache = true)
    void updateName(@Param("id") Long id, @Param("name") String name);
}
