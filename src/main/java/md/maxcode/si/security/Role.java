/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class Role implements GrantedAuthority {

    private Long id;
    private Long userId;
    private String authority;
    private String addedTime;
    private List<Privilege> privileges;

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String name_) {
        authority = name_;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id_) {
        id = id_;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId_) {
        userId = userId_;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges_) {
        this.privileges = privileges_;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(final String addedTime_) {
        addedTime = addedTime_;
    }

}