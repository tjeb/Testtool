/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.domain.AccessPoint;
import md.maxcode.si.domain.User;
import md.maxcode.si.domain.UserFile;
import md.maxcode.si.persistence.UserMapper;
import md.maxcode.si.security.Role;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {
    @Autowired
    private SqlSession sqlSession;

    @Autowired(required = true)
    private UserMapper userMapper;

    @Autowired(required = true)
    private FileService fileService;

    @Autowired(required = true)
    private AccessPointService accessPointService;

    @Autowired
    private TTSettings ttSettings;

    @Autowired
    private UtilsComponent utilsComponent;
    private Logger logger = Logger.getLogger(getClass().getName());

    public List<User> getAll() {
        return userMapper.getAll();
    }

    public List<User> getAllNonEditable() {
        return userMapper.getAllNonEditable();
    }

    public User getById(final Long id_) {
        return userMapper.getById(id_);
    }

    public User getByUsername(final String username_) {
        return userMapper.getByUsername(username_);
    }

    public User getByEmail(final String email) {
        return userMapper.getByEmail(email);
    }

    public User getByIdentifier(final String identifier) {
        return userMapper.getByIdentifier(identifier);
    }

    public List<Role> getRolesById(final Long id_) {
        return userMapper.getRolesById(id_);
    }

    public Long getIdByUsername(final String username_) {
        return userMapper.getIdByUsername(username_);
    }

    @Transactional
    /**
     * if id_ is null, then new record is going to be created
     */
    public User insertUser(final String name_,
                           final String companyName_,
                           final String identifier,
                           final String username_,
                           final String password_,
                           final Boolean enabled_,
                           final String type_,
                           final Boolean isAdmin_,
                           final Boolean isEditable_,
                           final String email
    ) {
        String salt = utilsComponent.getRandomString();
        String passwordHash = utilsComponent.getPasswordHash(password_, salt);

        User user = new User();
        user.setName(name_);
        user.setCompanyName(companyName_);
        user.setIdentifier(identifier);
        user.setUsername(username_);
        user.setEnabled(enabled_);
        user.setType(type_);
        user.setEditable(isEditable_);
        user.setSalt(salt);
        user.setEmail(email);

        if (isAdmin_) {
            user.setPassword(passwordHash);
        }

        userMapper.insertUser(user);
        String role = isAdmin_ ? "ROLE_ADMIN" : "ROLE_USER";
        userMapper.insertRole(user.getId(), role);


        if (!isAdmin_) {
            updateTempPassword(passwordHash, user.getId());
        }

        return user;
    }

    public void updateUser(final String name_, final String companyName_,
                           final String username_, final String identifier,
                           final Boolean enabled_, final String type_,
                           final String email, final String password, final Long id_) {
        User user = userMapper.getById(id_);
        user.setName(name_);
        user.setCompanyName(companyName_);
        user.setUsername(username_);
        user.setIdentifier(identifier);
        user.setEnabled(enabled_);
        user.setType(type_);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            String passwordHash = utilsComponent.getPasswordHash(password, user.getSalt());
            user.setPassword(null);
            updateTempPassword(passwordHash, user.getId());
        }

        userMapper.updateUser(user);
    }

    public void updateUser(final User user) {
        userMapper.updateUser(user);
    }

    public void remove(final Long userId) throws Exception {
        User user = getById(userId);
        if (!user.getEditable()) {
            throw new Exception("User cannot be deleted");
        }

        List<UserFile> userFiles = fileService.getByUserId(userId);

        for (UserFile file : userFiles) {
            fileService.deletePhysicalFile(ttSettings.storeLocation, file.getFileName());
            fileService.deleteUserFile(userId, file.getId());
        }

        List<AccessPoint> accessPoints = accessPointService.getAllByUserId(userId);
        for (AccessPoint accessPoint : accessPoints) {
            accessPointService.removeAP(accessPoint.getId(), userId);
        }

        userMapper.deleteRoleUser(userId);
        userMapper.deleteUser(userId);
    }

    public String getTempPassword(final Long id) {
        return userMapper.getTempPassword(id);
    }

    public void deleteTempPassword(final Long userId) {
        userMapper.deleteTempPassword(userId);
    }

    public void updateTempPassword(final String passwordHash, final Long userId) {
        userMapper.deleteTempPassword(userId);
        userMapper.writeTempPassword(userId, passwordHash);
    }

    public boolean updatePassword(final Long userId, final String newPassword) {
        User user = getById(userId);

        if (user == null) {
            logger.severe("Password cannot be changed, not-existent userId --> " + userId);
            return false;
        }

        deleteTempPassword(userId);

        final String passwordHash = utilsComponent.getPasswordHash(newPassword, user.getSalt());
        user.setPassword(passwordHash);
        updateUser(user);

        return true;
    }

    public void updateName(final Long id, final String name) {
        userMapper.updateName(id, name);
    }
}
